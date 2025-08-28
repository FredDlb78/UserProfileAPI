package com.example.todo.service;

import com.example.todo.UserProfile;
import com.example.todo.exception.ProfileNotFoundException;
import com.example.todo.repository.UserProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    @Mock
    UserProfileRepository repository;

    @InjectMocks
    UserProfileService service;

    @Test
    void updateTechnicalStack_sanitizesAndReplaces() {
        // given
        Long id = 1L;
        UserProfile existing = new UserProfile();
        existing.setId(id);
        existing.setFullName("Alice");
        existing.setTitle("QA");
        existing.setDescription("desc");
        existing.setExperienceYears(3);
        existing.setTechnicalStack(new ArrayList<>(List.of("Playwright"))); // état initial
        when(repository.findById(id)).thenReturn(Optional.of(existing));

        // when
        List<String> incoming = Arrays.asList(" Java ", "java", "Selenium", null, " ");
        UserProfile result = service.updateTechnicalStack(id, incoming);

        // then
        ArgumentCaptor<UserProfile> captor = ArgumentCaptor.forClass(UserProfile.class);
        verify(repository).save(captor.capture());
        UserProfile saved = captor.getValue();

        assertThat(saved.getTechnicalStack())
                .containsExactly("Java", "Selenium"); // trim + dédoublonnage + ordre conservé

        // et le résultat renvoyé reflète bien l’état
        assertThat(result.getTechnicalStack()).containsExactly("Java", "Selenium");
    }

    @Test
    void updateTechnicalStack_isIdempotent_whenNoChange() {
        // given
        Long id = 2L;
        UserProfile existing = new UserProfile();
        existing.setId(id);
        existing.setTechnicalStack(new ArrayList<>(List.of("Java", "Selenium")));
        when(repository.findById(id)).thenReturn(Optional.of(existing));

        // when
        UserProfile result = service.updateTechnicalStack(id, List.of("Java", "Selenium"));

        // then
        // pas d'appel à save() si rien ne change
        verify(repository, never()).save(any(UserProfile.class));
        assertThat(result.getTechnicalStack()).containsExactly("Java", "Selenium");
    }

    @Test
    void updateTechnicalStack_throws_whenNotFound() {
        // given
        Long missingId = 99L;
        when(repository.findById(missingId)).thenReturn(Optional.empty());

        // expect
        assertThatThrownBy(() -> service.updateTechnicalStack(missingId, List.of("Java")))
                .isInstanceOf(ProfileNotFoundException.class);
    }
}
