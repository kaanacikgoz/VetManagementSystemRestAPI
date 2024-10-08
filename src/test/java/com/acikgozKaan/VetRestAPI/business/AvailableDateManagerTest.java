package com.acikgozKaan.VetRestAPI.business;

import com.acikgozKaan.VetRestAPI.business.concretes.AvailableDateManager;
import com.acikgozKaan.VetRestAPI.core.exception.NotFoundException;
import com.acikgozKaan.VetRestAPI.core.utilies.Msg;
import com.acikgozKaan.VetRestAPI.dao.AvailableDateRepo;
import com.acikgozKaan.VetRestAPI.entity.AvailableDate;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AvailableDateManagerTest {

    @InjectMocks
    private AvailableDateManager availableDateManager;

    @Mock
    private AvailableDateRepo availableDateRepo;

    @Test
    void save_AvailableDate_Success() {
        // given
        AvailableDate willSaveAvailableDate = AvailableDate.builder()
                .id(1L)
                .build();

        // when
        when(availableDateRepo.save(willSaveAvailableDate)).thenReturn(willSaveAvailableDate);

        AvailableDate savedAvailableDate = availableDateManager.save(willSaveAvailableDate);

        // then
        assertThat(savedAvailableDate).isNotNull();
        assertThat(savedAvailableDate.getId()).isEqualTo(1L);
        verify(availableDateRepo).save(willSaveAvailableDate);
    }

    @Test
    void getAll_AvailableDates_Success() {
        // given
        AvailableDate availableDate = AvailableDate.builder()
                .id(1L)
                .build();

        AvailableDate availableDate2 = AvailableDate.builder()
                .id(2L)
                .build();

        // when
        when(availableDateRepo.findAll()).thenReturn(List.of(availableDate, availableDate2));

        List<AvailableDate> foundAvailableDates = availableDateManager.getAll();

        // then
        assertThat(foundAvailableDates).hasSize(2);
        assertThat(foundAvailableDates).extracting(AvailableDate::getId)
                .containsExactlyInAnyOrder(1L, 2L);
    }

    @Nested
    final class getByIdTest {

        @Test
        void getById_AvailableDate_Success() {
            // given
            AvailableDate availableDate = AvailableDate.builder()
                    .id(1L)
                    .build();

            // when
            when(availableDateRepo.findById(availableDate.getId())).thenReturn(Optional.of(availableDate));

            AvailableDate foundAvailableDate = availableDateManager.getById(availableDate.getId());

            // then
            assertThat(foundAvailableDate).isNotNull();
            assertThat(foundAvailableDate.getId()).isEqualTo(1L);
        }

        @Test
        void getById_AvailableDate_ThrowsNotFoundException() {
            // given
            Long nonExistenceAvailableDateId = 2L;

            // when
            when(availableDateRepo.findById(nonExistenceAvailableDateId)).thenReturn(Optional.empty());

            Exception thrown = assertThrows(NotFoundException.class, ()->availableDateManager.getById(nonExistenceAvailableDateId));

            // then
            assertThat(thrown).isInstanceOf(NotFoundException.class);
            assertThat(thrown.getMessage()).isEqualTo(Msg.NOT_FOUND);
        }

    }

    @Test
    void update_AvailableDate_Success() {
        // given
        AvailableDate existinceAvailableDate = AvailableDate.builder()
                .id(1L)
                .availableDate(LocalDate.of(2024,9,9))
                .build();

        // when
        when(availableDateRepo.findById(1L)).thenReturn(Optional.of(existinceAvailableDate));

        existinceAvailableDate.setAvailableDate(LocalDate.of(2024,10,10));
        when(availableDateRepo.save(existinceAvailableDate)).thenReturn(existinceAvailableDate);

        AvailableDate updatedAvailableDate = availableDateManager.update(existinceAvailableDate);

        // then
        assertThat(updatedAvailableDate.getAvailableDate()).isEqualTo(LocalDate.of(2024,10,10));
        verify(availableDateRepo).save(updatedAvailableDate);
    }

    @Test
    void delete_AvailableDate_Success() {
        // given
        AvailableDate existinceAvailableDate = AvailableDate.builder()
                .id(1L)
                .availableDate(LocalDate.of(2024,9,9))
                .build();

        // when
        when(availableDateRepo.findById(1L)).thenReturn(Optional.of(existinceAvailableDate));

        availableDateManager.delete(existinceAvailableDate.getId());

        // then
        verify(availableDateRepo).findById(existinceAvailableDate.getId());
        verify(availableDateRepo).delete(existinceAvailableDate);
    }

    @Test
    void findByIds_AvailableDates_Success() {
        // given
        AvailableDate availableDate = AvailableDate.builder()
                .id(1L)
                .build();

        AvailableDate availableDate2 = AvailableDate.builder()
                .id(2L)
                .build();

        // when
        when(availableDateRepo.findAllById(List.of(1L,2L))).thenReturn(List.of(availableDate, availableDate2));

        List<AvailableDate> foundAvailableDates = availableDateManager.findByIds(List.of(1L,2L));

        // then
        assertThat(foundAvailableDates).hasSize(2);
        assertThat(foundAvailableDates).extracting(AvailableDate::getId)
                .containsExactlyInAnyOrder(1L, 2L);
    }

}
