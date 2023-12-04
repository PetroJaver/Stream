package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        new Main.VisitCounter().count(
                new HashMap<>() {
                    {
                        put("firstMicroservice", new Main.UserStats(12L));
                        put("firstMicroservice", new Main.UserStats(-10L));
                        put("1", null);
                        put("2", new Main.UserStats(12L));
                    }
                },
                new HashMap<>() {
                    {
                        put("4", new Main.UserStats(12L));
                        put("7", new Main.UserStats(13L));
                        put("1", new Main.UserStats(10L));
                        put("2", new Main.UserStats(12L));
                    }
                },
                new HashMap<>() {
                    {
                        put("firstMicroservice", new Main.UserStats(12L));
                        put("firstMicroservice", new Main.UserStats(-10L));
                        put("bsd1.3", null);
                        put("3", new Main.UserStats(12L));
                    }
                },
                new HashMap<>() {
                    {
                        put("5", new Main.UserStats(12L));
                        put("4", new Main.UserStats(-10L));
                        put("1", null);
                        put("2", new Main.UserStats(-1L));
                    }
                },
                new HashMap<>() {
                    {
                        put(null, null);
                        put("2", new Main.UserStats(-10L));
                        put("1", null);
                        put(null, new Main.UserStats(12L));
                    }
                }).entrySet().forEach(System.out::println);
    }

    public static class VisitCounter {

        Map<Long, Long> count(Map<String, UserStats>... visits) {

            return Arrays.stream(visits)
                    .map(map -> Objects.isNull(map) ? new HashMap<String, UserStats>() : map)
                    .flatMap(map -> map.entrySet().stream())
                    .filter(entry -> Objects.nonNull(entry.getValue()))
                    .filter(entry -> entry.getValue().getVisitCount().isPresent())
                    .filter(this::isParseableToLong)
                    .map(entry -> Map.entry(Long.parseLong(entry.getKey()), entry.getValue().getVisitCount().get()))
                    .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingLong(Map.Entry::getValue)));
        }

        private boolean isParseableToLong(Map.Entry<String, UserStats> entry) {
            try {
                Long.parseLong(entry.getKey());
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }


    public static class UserStats {
        public UserStats(Long visitCount) {
            if (visitCount < 0) {
                this.visitCount = Optional.empty();
            } else {
                this.visitCount = Optional.of(visitCount);
            }
        }

        private Optional<Long> visitCount;

        public Optional<Long> getVisitCount() {
            return visitCount;
        }
    }
}