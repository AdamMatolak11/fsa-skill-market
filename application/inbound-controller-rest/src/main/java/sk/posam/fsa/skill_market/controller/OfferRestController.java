package sk.posam.fsa.skill_market.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import sk.posam.fsa.skill_market.domain.offer.Offer;
import sk.posam.fsa.skill_market.domain.offer.OfferCommandRepository;
import sk.posam.fsa.skill_market.domain.offer.OfferNotFoundException;
import sk.posam.fsa.skill_market.domain.project.Project;
import sk.posam.fsa.skill_market.domain.project.ProjectNotFoundException;
import sk.posam.fsa.skill_market.domain.project.ProjectQueryRepository;
import sk.posam.fsa.skill_market.domain.service.OfferFacade;
import sk.posam.fsa.skill_market.mapper.OfferRestMapper;
import sk.posam.fsa.skill_market.rest.api.OffersApi;
import sk.posam.fsa.skill_market.rest.dto.CreateOfferRequest;
import sk.posam.fsa.skill_market.rest.dto.OfferResponse;
import sk.posam.fsa.skill_market.security.AuthenticatedUser;
import sk.posam.fsa.skill_market.security.AuthenticatedUserProvider;
import sk.posam.fsa.skill_market.security.ForbiddenOperationException;

@RestController
public class OfferRestController implements OffersApi {

    private final OfferFacade offerFacade;
    private final OfferRestMapper offerRestMapper;
    private final ProjectQueryRepository projectQueryRepository;
    private final OfferCommandRepository offerCommandRepository;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    public OfferRestController(
            OfferFacade offerFacade,
            OfferRestMapper offerRestMapper,
            ProjectQueryRepository projectQueryRepository,
            OfferCommandRepository offerCommandRepository,
            AuthenticatedUserProvider authenticatedUserProvider
    ) {
        this.offerFacade = offerFacade;
        this.offerRestMapper = offerRestMapper;
        this.projectQueryRepository = projectQueryRepository;
        this.offerCommandRepository = offerCommandRepository;
        this.authenticatedUserProvider = authenticatedUserProvider;
    }

    @Override
    public ResponseEntity<List<OfferResponse>> getProjectOffers(UUID projectId) {
        assertCanManageProjectOffers(projectId);
        List<OfferResponse> body = offerFacade.getProjectOffers(projectId).stream()
                .map(offerRestMapper::toResponse)
                .toList();
        return ResponseEntity.ok(body);
    }

    @Override
    public ResponseEntity<OfferResponse> createOffer(UUID projectId, CreateOfferRequest createOfferRequest) {
        assertCanCreateOffer(createOfferRequest.getFreelancerId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(offerRestMapper.toResponse(
                        offerFacade.createOffer(offerRestMapper.toCommand(projectId, createOfferRequest))
                ));
    }

    @Override
    public ResponseEntity<OfferResponse> acceptOffer(UUID projectId, UUID offerId) {
        assertCanManageProjectOffers(projectId);
        return ResponseEntity.ok(offerRestMapper.toResponse(offerFacade.acceptOffer(projectId, offerId)));
    }

    @Override
    public ResponseEntity<OfferResponse> rejectOffer(UUID projectId, UUID offerId) {
        assertCanManageProjectOffers(projectId);
        return ResponseEntity.ok(offerRestMapper.toResponse(offerFacade.rejectOffer(projectId, offerId)));
    }

    @Override
    public ResponseEntity<Void> deleteOffer(UUID projectId, UUID offerId) {
        assertCanCancelOffer(projectId, offerId);
        offerFacade.deleteOffer(projectId, offerId);
        return ResponseEntity.noContent().build();
    }

    private void assertCanCreateOffer(UUID freelancerId) {
        Optional<AuthenticatedUser> currentUser = authenticatedUserProvider.currentUser();
        if (currentUser.isEmpty()) {
            return;
        }

        AuthenticatedUser user = currentUser.get();
        if (!user.hasRole("ADMIN") && !user.userId().equals(freelancerId)) {
            throw new ForbiddenOperationException("Current user cannot create offers for freelancer '" + freelancerId + "'");
        }
    }

    private void assertCanManageProjectOffers(UUID projectId) {
        Project project = projectQueryRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        Optional<AuthenticatedUser> currentUser = authenticatedUserProvider.currentUser();
        if (currentUser.isEmpty()) {
            return;
        }

        AuthenticatedUser user = currentUser.get();
        if (user.hasRole("ADMIN")) {
            return;
        }
        if (project.clientId() != null && !project.clientId().equals(user.userId())) {
            throw new ForbiddenOperationException("Current user cannot manage offers for project '" + projectId + "'");
        }
    }

    private void assertCanCancelOffer(UUID projectId, UUID offerId) {
        Offer offer = offerCommandRepository.findById(offerId)
                .orElseThrow(() -> new OfferNotFoundException(offerId));
        if (!offer.projectId().equals(projectId)) {
            throw new OfferNotFoundException(offerId);
        }

        Optional<AuthenticatedUser> currentUser = authenticatedUserProvider.currentUser();
        if (currentUser.isEmpty()) {
            return;
        }

        AuthenticatedUser user = currentUser.get();
        if (user.hasRole("ADMIN")) {
            return;
        }
        if (!user.userId().equals(offer.freelancerId())) {
            throw new ForbiddenOperationException("Current user cannot cancel offer '" + offerId + "'");
        }
    }
}
