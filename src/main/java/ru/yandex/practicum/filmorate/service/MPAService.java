package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.mpa.MPAResponse;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.mapper.MPAMapper;
import ru.yandex.practicum.filmorate.storage.MPAStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MPAService {
    private final MPAStorage storage;
    private final MPAMapper mapper;

    public List<MPAResponse> getAll() {
        log.debug("Передача запроса на получение всех рейтингов в контейнер");
        List<MPA> result = storage.getAll();
        return result.stream()
                .map(mapper::toMPAResponse)
                .toList();
    }

    public MPAResponse get(long id) {
        storage.checkId(id);
        log.debug("Передача запроса на получение рейтинга с id = {} в контейнер", id);
        MPA result = storage.get(id);
        return mapper.toMPAResponse(result);
    }
}
