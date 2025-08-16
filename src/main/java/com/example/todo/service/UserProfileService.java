package com.example.todo.service;

import com.example.todo.UserProfile;
import com.example.todo.exception.ProfileNotFoundException;
import com.example.todo.repository.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserProfileService {

    private final UserProfileRepository repository;

    public UserProfileService(UserProfileRepository repository) {
        this.repository = repository;
    }

    /* =======================
       =====  READ (R)  ======
       ======================= */

    @Transactional(readOnly = true)
    public List<UserProfile> getAllProfiles() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public UserProfile getProfileById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException(id));
    }

    /* =======================
       ===== CREATE (C) ======
       ======================= */

    @Transactional
    public UserProfile createProfile(UserProfile profile) {
        if (profile.getTechnicalStack() == null) {
            profile.setTechnicalStack(new ArrayList<>());
        } else {
            profile.setTechnicalStack(new ArrayList<>(sanitizeSkills(profile.getTechnicalStack())));
        }
        return repository.save(profile);
    }

    /* =======================
       ===== UPDATE (U) ======
       ======================= */

    @Transactional
    public UserProfile updateProfile(Long id, UserProfile updated) {
        UserProfile existing = getProfileById(id);

        existing.setFullName(updated.getFullName());
        existing.setTitle(updated.getTitle());
        existing.setDescription(updated.getDescription());
        existing.setExperienceYears(updated.getExperienceYears());

        // Si le payload porte aussi la pile technique, on remplace proprement
        if (updated.getTechnicalStack() != null) {
            List<String> sanitized = sanitizeSkills(updated.getTechnicalStack());
            existing.getTechnicalStack().clear();
            existing.getTechnicalStack().addAll(sanitized);
        }

        return repository.save(existing);
    }

    /**
     * PATCH ciblé : remplace uniquement le technicalStack d’un profil.
     */
    @Transactional
    public UserProfile updateTechnicalStack(Long id, List<String> incomingSkills) {
        UserProfile existing = getProfileById(id);

        List<String> sanitized = sanitizeSkills(incomingSkills);

        if (Objects.equals(existing.getTechnicalStack(), sanitized)) {
            return existing;
        }

        existing.getTechnicalStack().clear();
        existing.getTechnicalStack().addAll(sanitized);

        return repository.save(existing);
    }

    /* =======================
       ===== DELETE (D) ======
       ======================= */

    @Transactional
    public void deleteProfile(Long id) {
        if (!repository.existsById(id)) {
            throw new ProfileNotFoundException(id);
        }
        repository.deleteById(id);
    }

    /* =======================
       ===== Helpers 🔧  ======
       ======================= */

    /**
     * Nettoie/normalise la liste de skills :
     * - trim
     * - supprime null/vides
     * - dédoublonne insensible à la casse
     * - conserve l’ordre d’entrée
     */
    private List<String> sanitizeSkills(List<String> input) {
        if (input == null) return new ArrayList<>();
        Set<String> seenLower = new HashSet<>();
        List<String> out = new ArrayList<>();
        for (String s : input) {
            if (s == null) continue;
            String t = s.trim();
            if (t.isEmpty()) continue;
            String key = t.toLowerCase(Locale.ROOT);
            if (seenLower.add(key)) {
                out.add(t);
            }
        }
         out.sort(String.CASE_INSENSITIVE_ORDER);
        return out;
    }
}