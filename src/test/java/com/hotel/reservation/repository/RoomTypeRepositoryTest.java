package com.hotel.reservation.repository;

import com.hotel.reservation.entity.RoomType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.TransactionSystemException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

//@DataJpaTest
//@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class RoomTypeRepositoryTest {

    @Autowired
    private RoomTypeRepository underTest;

    @Test
    void itShouldCheckWhenRoomTypeByIdExists() {
        // Given
        String label = "abc";
        RoomType roomType = new RoomType(label, "empty");

        // When
        RoomType save = underTest.save(roomType);

        // Then
        assertThat(underTest.existsById(save.getId())).isTrue();
    }

    @Test
    void itShouldNotSelectRoomTypeByIdWhenIdDoesNotExist() {
        // Given
        Long id = 123432L;

        // Then
        assertThat(underTest.existsById(id)).isFalse();
    }

    @Test
    void itShouldSelectRoomTypeByLabel() {
        // Given
        String label = "abc";
        RoomType roomType = new RoomType(label, "empty");

        // When
        underTest.save(roomType);

        // Then
        assertThat(underTest.existsByLabel(label)).isTrue();
    }

    @Test
    void itShouldNotSelectRoomTypeByLabelWhenLabelDoesNotExist() {
        // Given
        String label = UUID.randomUUID().toString();

        // Then
        assertThat(underTest.existsByLabel(label)).isFalse();
    }

    @Test
    void itShouldSaveRoomType() {
        // Given
        String label = "abc";
        RoomType roomType = new RoomType(label, "empty");

        // When
        RoomType save = underTest.save(roomType);

        // Then
        Optional<RoomType> optionalRoomType = underTest.findById(save.getId());
        assertThat(optionalRoomType)
                .isPresent();
    }

    @Test
    void itShouldNotSaveRoomTypeWhenLabelIsNull() {
        // Given
        RoomType roomType = new RoomType(null, "empty");


        // When
        // Then
        assertThatThrownBy(() -> underTest.save(roomType))
                .hasMessageContaining("Could not commit JPA transaction; nested exception is javax.persistence.RollbackException: Error while committing the transaction")
                .isInstanceOf(TransactionSystemException.class);

    }

}
