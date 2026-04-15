package sk.posam.fsa.skill_market.architecture;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class HexagonalArchitectureTest {

    private static final JavaClasses CLASSES = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("sk.posam.fsa.skill_market");

    @Test
    void domain_must_not_depend_on_frameworks_or_adapters() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                        "org.springframework..",
                        "jakarta.persistence..",
                        "org.hibernate..",
                        "sk.posam.fsa.skill_market.controller..",
                        "sk.posam.fsa.skill_market.mapper..",
                        "sk.posam.fsa.skill_market.security..",
                        "sk.posam.fsa.skill_market.jpa..",
                        "sk.posam.fsa.skill_market.rest..",
                        "sk.posam.fsa.skill_market")
                .because("domain must stay framework-free and unaware of adapters and runtime");

        rule.check(CLASSES);
    }

    @Test
    void inbound_layer_must_not_depend_on_outbound_adapter_or_runtime() {
        ArchRule rule = noClasses()
                .that().resideInAnyPackage(
                        "sk.posam.fsa.skill_market.controller..",
                        "sk.posam.fsa.skill_market.mapper..",
                        "sk.posam.fsa.skill_market.security..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                        "sk.posam.fsa.skill_market.jpa..",
                        "sk.posam.fsa.skill_market")
                .because("inbound adapters should talk to the domain, not directly to JPA or runtime wiring");

        rule.check(CLASSES);
    }

    @Test
    void outbound_layer_must_not_depend_on_inbound_api_contract_or_runtime() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("sk.posam.fsa.skill_market.jpa..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                        "sk.posam.fsa.skill_market.controller..",
                        "sk.posam.fsa.skill_market.mapper..",
                        "sk.posam.fsa.skill_market.security..",
                        "sk.posam.fsa.skill_market.rest..",
                        "sk.posam.fsa.skill_market")
                .because("outbound adapters should implement domain ports without depending on inbound or runtime");

        rule.check(CLASSES);
    }

    @Test
    void runtime_package_is_the_only_place_that_may_depend_on_both_adapters() {
        ArchRule rule = noClasses()
                .that().resideInAnyPackage(
                        "sk.posam.fsa.skill_market.domain..",
                        "sk.posam.fsa.skill_market.controller..",
                        "sk.posam.fsa.skill_market.mapper..",
                        "sk.posam.fsa.skill_market.security..",
                        "sk.posam.fsa.skill_market.jpa..",
                        "sk.posam.fsa.skill_market.rest..")
                .should().dependOnClassesThat()
                .resideInAPackage("sk.posam.fsa.skill_market")
                .because("runtime wiring belongs only to the springboot module");

        rule.check(CLASSES);
    }

    @Test
    void controllers_must_be_explicit_rest_entry_points() {
        ArchRule rule = classes()
                .that().resideInAPackage("sk.posam.fsa.skill_market.controller..")
                .and().areTopLevelClasses()
                .should().beAnnotatedWith(RestController.class)
                .because("controller package should only contain REST entry points");

        rule.check(CLASSES);
    }

    @Test
    void repository_ports_and_adapters_must_follow_hexagonal_naming_and_roles() {
        ArchRule domainPorts = classes()
                .that().resideInAPackage("sk.posam.fsa.skill_market.domain.project")
                .and().haveSimpleNameEndingWith("Repository")
                .should().beInterfaces()
                .because("domain repositories are outbound ports and must be interfaces");

        ArchRule outboundAdapters = classes()
                .that().resideInAPackage("sk.posam.fsa.skill_market.jpa..")
                .and().haveSimpleNameEndingWith("RepositoryAdapter")
                .should().beAnnotatedWith(Repository.class)
                .andShould(implementDomainRepositoryPort())
                .because("outbound repository adapters must implement a domain port");

        domainPorts.check(CLASSES);
        outboundAdapters.check(CLASSES);
    }

    @Test
    void spring_data_repositories_must_stay_internal_to_outbound_module() {
        ArchRule rule = classes()
                .that().resideInAPackage("sk.posam.fsa.skill_market.jpa..")
                .and().haveSimpleNameEndingWith("SpringDataRepository")
                .should().beInterfaces()
                .andShould().notBePublic()
                .because("Spring Data repositories are internal outbound details");

        rule.check(CLASSES);
    }

    @Test
    void runtime_root_should_only_contain_application_and_configuration_classes() {
        ArchRule rule = classes()
                .that().resideInAPackage("sk.posam.fsa.skill_market")
                .should().beAnnotatedWith(org.springframework.context.annotation.Configuration.class)
                .orShould().beAnnotatedWith(SpringBootApplication.class)
                .because("runtime root package should contain only bootstrapping and bean configuration");

        rule.check(CLASSES);
    }

    private static ArchCondition<JavaClass> implementDomainRepositoryPort() {
        return new ArchCondition<>("implement a domain repository port") {
            @Override
            public void check(JavaClass item, ConditionEvents events) {
                Set<String> repositoryInterfaces = item.getAllRawInterfaces().stream()
                        .map(JavaClass::getFullName)
                        .filter(name -> name.startsWith("sk.posam.fsa.skill_market.domain."))
                        .filter(name -> name.endsWith("Repository"))
                        .collect(Collectors.toSet());

                boolean satisfied = !repositoryInterfaces.isEmpty();
                String message = item.getName() + (satisfied
                        ? " implements domain port " + repositoryInterfaces
                        : " does not implement any domain repository port");
                events.add(new SimpleConditionEvent(item, satisfied, message));
            }
        };
    }
}
