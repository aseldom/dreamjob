package ru.job4j.dreamjob.repository;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@ThreadSafe
public class MemoryCandidateRepository implements CandidateRepository {

    @GuardedBy("this")
    private final AtomicInteger atomicInt = new AtomicInteger(1);

    private final ConcurrentMap<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private MemoryCandidateRepository() {
        save(new Candidate(0, "Experienced Java Developer", "", LocalDateTime.now(), 0, 0));
        save(new Candidate(0, "Middle+ Java Developer", "", LocalDateTime.now(), 0, 0));
        save(new Candidate(0, "Senior Java Developer", "", LocalDateTime.now(), 0, 0));
    }

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(atomicInt.getAndIncrement());
        candidates.putIfAbsent(candidate.getId(), candidate);
        return candidate;
    }

    @Override
    public boolean deleteById(int id) {
        return candidates.remove(id, candidates.get(id));
    }

    @Override
    public boolean update(Candidate candidate) {
        return candidates.computeIfPresent(candidate.getId(), (id, oldCandidate) ->
                new Candidate(
                        oldCandidate.getId(),
                        candidate.getName(),
                        candidate.getDescription(),
                        candidate.getCreationDate(),
                        candidate.getCityId(),
                        candidate.getFileId()
                )
        ) != null;
    }

    @Override
    public Optional<Candidate> findById(int id) {
        return Optional.ofNullable(candidates.get(id));
    }

    @Override
    public Collection<Candidate> findAll() {
        return candidates.values();
    }

}