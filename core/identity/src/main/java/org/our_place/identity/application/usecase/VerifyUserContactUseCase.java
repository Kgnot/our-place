package org.our_place.identity.application.usecase;

import lombok.RequiredArgsConstructor;
import org.our_place.identity.api.event.UserContactVerifiedEvent;
import org.our_place.identity.application.usecase.command.VerifyUserContactCommand;
import org.our_place.identity.application.usecase.output.VerifyUserContactOutput;
import org.our_place.identity.domain.entity.UserContact;
import org.our_place.identity.domain.entity.UserContactId;
import org.our_place.identity.domain.exception.UserContactNotFoundException;
import org.our_place.identity.infra.repository.UserContactRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class VerifyUserContactUseCase implements UseCase<VerifyUserContactCommand, VerifyUserContactOutput> {

    private final UserContactRepository userContactRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public VerifyUserContactOutput execute(VerifyUserContactCommand command) {
        UserContactId id = new UserContactId(command.userLoginId(), command.contactTypeCode(), command.value());

        UserContact contact = userContactRepository.findById(id)
                .orElseThrow(UserContactNotFoundException::new);

        contact.verify();

        eventPublisher.publishEvent(new UserContactVerifiedEvent(
                command.userLoginId(), command.contactTypeCode(), command.value()
        ));

        return new VerifyUserContactOutput(true);
    }
}