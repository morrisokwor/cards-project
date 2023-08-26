package com.okworo.cards.services;

import com.okworo.cards.dao.request.CardRequest;
import com.okworo.cards.dao.request.CardSearchModel;
import com.okworo.cards.dao.response.CardResponse;
import com.okworo.cards.entities.CardEntity;
import com.okworo.cards.entities.UserEntity;
import com.okworo.cards.enums.CardStatus;
import com.okworo.cards.enums.Role;
import com.okworo.cards.enums.SortOrder;
import com.okworo.cards.exceptions.CustomValidationException;
import com.okworo.cards.repository.CardRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.okworo.cards.entities.QCardEntity.cardEntity;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;

    @PersistenceContext
    EntityManager entityManager;


    public CardResponse addCard(CardRequest cardRequest) {

        Optional<String> color = Optional.ofNullable(cardRequest.getColor());
        if (color.isPresent() && cardRequest.getColor() != null && !validateColor(color.get())) {
            throw new CustomValidationException("Color format should be # followed by 6 alphanumeric characters");
        }

        CardEntity card = CardEntity.builder()
                .color(color.isPresent() ? color.get() : null)
                .name(cardRequest.getName())
                .status(CardStatus.TO_DO)
                .description(cardRequest.getDescription())
                .build();

        return toDTO(cardRepository.save(card));
    }

    public CardResponse updateCard(CardRequest cardRequest) {

        UserEntity user = currentUser();

        CardEntity card = cardRepository.findById(cardRequest.getId()).orElseThrow(() ->
                new CustomValidationException("Card wit Id: " + cardRequest.getId() + " Not Found"));

        if (card.getCreatedBy() != user.getId() && !user.getRole().equals(Role.ADMIN)) {
            throw new CustomValidationException("YOu do not have privilege to edit the card");

        }

        Optional<String> color = Optional.ofNullable(cardRequest.getColor());
        if (color.isPresent() && cardRequest.getColor() != null && !validateColor(color.get())) {
            throw new CustomValidationException("Color format should be # followed by 6 alphanumeric characters");
        }

        card.setColor(color.isPresent() ? color.get() : null);
        card.setName(cardRequest.getName());
        card.setStatus(CardStatus.fromStatus(cardRequest.getStatus()));
        card.setDescription(cardRequest.getDescription());

        CardEntity savedCard = cardRepository.save(card);

        return toDTO(savedCard);
    }

    public List<CardResponse> search(CardSearchModel searchModel) {

        int offset = 1;
        Optional<Integer> pageNo = Optional.ofNullable(searchModel.getPageNo());
        Optional<Integer> pageSize = Optional.ofNullable(searchModel.getPageSize());
        Optional<String> sortBy = Optional.ofNullable(searchModel.getSortBy());
        Optional<SortOrder> sortOrder = Optional.ofNullable(searchModel.getSortOrder());


        if (pageNo.isPresent() && pageSize.isPresent()) {
            offset = (pageNo.get() - 1) * pageSize.get();
        }
        JPAQuery<CardEntity> jpaQuery = new JPAQuery<>(entityManager);
        BooleanBuilder builder = new BooleanBuilder();

        Optional.ofNullable(searchModel.getId()).ifPresent(
                val -> builder.and(cardEntity.id.eq(val))
        );
        Optional.ofNullable(searchModel.getName()).ifPresent(
                val -> builder.and(cardEntity.name.likeIgnoreCase(val + "%"))
        );
        Optional.ofNullable(searchModel.getColor()).ifPresent(
                val -> builder.and(cardEntity.color.likeIgnoreCase("%" + val + "%"))
        );
        Optional.ofNullable(searchModel.getStatus()).ifPresent(
                val -> builder.and(cardEntity.status.eq(CardStatus.fromStatus(val)))
        );

        Optional.ofNullable(searchModel.getCreatedAt()).ifPresent(
                val -> builder.and(cardEntity.createdAt.eq(val))
        );

        List<CardEntity> entities = new ArrayList<>();
        OrderSpecifier<?> orderSpecifier = cardEntity.id.desc(); // Default sorting by ID

        if (sortBy.isPresent() && sortBy != null) {

            switch (searchModel.getSortBy()) {
                case "name":
                    if (sortOrder.isPresent() && sortOrder.get().equals(SortOrder.DESC))
                        orderSpecifier = cardEntity.name.desc();
                    else
                        orderSpecifier = cardEntity.name.asc();

                    break;
                case "color":
                    if (sortOrder.isPresent() && sortOrder.get().equals(SortOrder.DESC))
                        orderSpecifier = cardEntity.color.desc();
                    else
                        orderSpecifier = cardEntity.color.asc(); // Sorting by color ascending
                    break;
                case "status":
                    if (sortOrder.isPresent() && sortOrder.get().equals(SortOrder.DESC))

                        orderSpecifier = cardEntity.status.desc();
                    else
                        orderSpecifier = cardEntity.status.asc();

                case "createdAt":
                    if (sortOrder.isPresent() && sortOrder.get().equals(SortOrder.DESC))
                        orderSpecifier = cardEntity.createdAt.desc();
                    else
                        orderSpecifier = cardEntity.createdAt.asc();
                    break;
            }
        }


        if (pageNo.isPresent() && pageSize.isPresent()) {
            entities = jpaQuery
                    .from(cardEntity)
                    .select(cardEntity)
                    .where(builder)
                    .orderBy(orderSpecifier)
                    .offset(offset)
                    .limit(pageSize.get())
                    .fetch();
        } else {
            entities = jpaQuery
                    .from(cardEntity)
                    .select(cardEntity)
                    .where(builder)
                    .orderBy(orderSpecifier)
                    .fetch();
        }
        return entities.stream().map(card -> toDTO(card)).collect(Collectors.toList());
    }


    public CardResponse findById(Long id) {
        CardEntity card = cardRepository.findById(id).orElseThrow(() ->
                new CustomValidationException("Card wit Id: " + id + " Not Found"));

        return toDTO(card);
    }

    public List<CardResponse> getAllCards() {
        UserEntity user = this.currentUser();

        List<CardResponse> cardResponses = new ArrayList<>();

        if (user.getRole().equals(Role.ADMIN)) {

            cardResponses = cardRepository.findAll().stream().map(card -> toDTO(card)).collect(Collectors.toList());
        } else if (user.getRole().equals(Role.MEMBER)) {
            cardResponses = cardRepository.findByCreatedBy(user.getId()).stream().map(card -> toDTO(card)).collect(Collectors.toList());
        }

        return cardResponses;
    }

    public void deleteCard(Long id) {
        CardEntity card = cardRepository.findById(id).orElseThrow(() ->
                new CustomValidationException("Card wit Id: " + id + " Not Found"));

        UserEntity user = currentUser();

        if (card.getCreatedBy() != user.getId() && !user.getRole().equals(Role.ADMIN)) {
            throw new CustomValidationException("YOu do not have privilege to edit the card");

        }

        cardRepository.delete(card);
    }


    public CardResponse toDTO(CardEntity card) {
        return CardResponse.builder()
                .color(card.getColor())
                .status(card.getStatus().getStatus())
                .name(card.getName())
                .id(card.getId())
                .description(card.getDescription())
                .createdAt(card.getCreatedAt())
                .createdBy(card.getCreatedBy())
                .updatedAt(card.getUpdatedAt())
                .updatedBy(card.getUpdatedBy())
                .build();
    }

    public boolean validateColor(String colorCode) {
        if (!Pattern.matches("^#[0-9A-Fa-f]{6}$", colorCode)) {
            return false;
        }
        return true;
    }

    public UserEntity currentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        return (UserEntity) context.getAuthentication().getPrincipal();
    }
}
