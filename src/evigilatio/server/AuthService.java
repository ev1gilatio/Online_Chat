package evigilatio.server;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AuthService {
    private static final List<Entry> entries;

    static {
        entries = List.of(
                new Entry("Evig", "pass"),
                new Entry("Rom", "pass"),
                new Entry("Max", "pass")
        );
    }

    public Optional<Entry> findUserByLoginAndPassword(String login, String password) {
        for (AuthService.Entry entry : entries) {
            if (entry.login.equals(login) && entry.password.equals(password)) {
                return Optional.of(entry);
            }
        }

        return Optional.empty();
    }

    static class Entry {
        String login;
        String password;

        Entry(String login, String password) {
            this.login = login;
            this.password = password;
        }

        String getLogin() {
            return login;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Entry entry = (Entry) o;
            return Objects.equals(login, entry.login) && Objects.equals(password, entry.password);
        }

        @Override
        public int hashCode() {
            return Objects.hash(login, password);
        }
    }
}
