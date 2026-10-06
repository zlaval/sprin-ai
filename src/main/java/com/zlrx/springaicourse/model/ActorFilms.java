package com.zlrx.springaicourse.model;

import java.util.List;

public record ActorFilms(String name, int age, List<Film> films) {
}
