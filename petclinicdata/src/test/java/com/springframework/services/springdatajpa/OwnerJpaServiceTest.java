package com.springframework.services.springdatajpa;

import com.springframework.model.Owner;
import com.springframework.repositories.OwnerRepository;
import com.springframework.repositories.PetRepository;
import com.springframework.repositories.PetTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerJpaServiceTest {

    private static final String LAST_NAME = "smith";
    @Mock
    OwnerRepository ownerRepository;
    @Mock
    PetRepository petRepository;
    @Mock
    PetTypeRepository petTypeRepository;

    @InjectMocks
    OwnerJpaService ownerJpaService;

    Owner returnOwner;

    @BeforeEach
    void setUp() {
        returnOwner = Owner.builder().id(1L).lastName(LAST_NAME).build();
    }

    @Test
    void findAll() {
        Set<Owner> returnOwnerSet = new HashSet<>();
        returnOwnerSet.add(Owner.builder().id(1L).build());
        returnOwnerSet.add(Owner.builder().id(2L).build());

        when(ownerRepository.findAll()).thenReturn(returnOwnerSet);
        Set<Owner> owners = ownerJpaService.findAll();

        assertNotNull(owners);
        assertEquals(2, owners.size());
    }

    @Test
    void findById() {
        when(ownerRepository.findById(anyLong())).thenReturn(Optional.of(returnOwner));

        Owner owner = ownerJpaService.findById(1L);

        assertNotNull(owner);
    }

    @Test
    void findByIdNotFound() {
        when(ownerRepository.findById(anyLong())).thenReturn(Optional.empty());

        Owner owner = ownerJpaService.findById(1L);

        assertNull(owner);
    }

    @Test
    void save() {
        Owner ownerToSave = Owner.builder().id(1L).build();
        when(ownerRepository.save(any())).thenReturn(returnOwner);
        Owner savedOwner = ownerJpaService.save(ownerToSave);
        assertNotNull(savedOwner);
        verify(ownerRepository).save(any());

    }

    @Test
    void delete() {
        ownerJpaService.delete(returnOwner);
        //default it 1 time
        verify(ownerRepository, times(1)).delete(any());
    }

    @Test
    void deleteById() {
        ownerJpaService.deleteById(1L);
        verify(ownerRepository).deleteById(anyLong());
    }

    @Test
    void findByLastName() {
        when(ownerRepository.findByLastName(any())).thenReturn(returnOwner);

        Owner smith = ownerJpaService.findByLastName(LAST_NAME);

        assertEquals(LAST_NAME, smith.getLastName());

        verify(ownerRepository).findByLastName(any());
    }
}