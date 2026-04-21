package sk.posam.fsa.skill_market.domain.project;

public class ProjectAlreadyExistsException extends RuntimeException {

    public ProjectAlreadyExistsException(String title) {
        super("Project with title '" + title + "' already exists");
    }
}
