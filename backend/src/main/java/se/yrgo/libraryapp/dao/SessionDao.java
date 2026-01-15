package se.yrgo.libraryapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import javax.inject.Inject;
import javax.sql.DataSource;
import org.pac4j.core.exception.CredentialsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.yrgo.libraryapp.entities.UserId;

public class SessionDao {
    private static Logger logger = LoggerFactory.getLogger(SessionDao.class);
    private static final Duration SESSION_EXPIRATION = Duration.ofDays(7);

    private DataSource ds;

    @Inject
    SessionDao(DataSource ds) {
        this.ds = ds;
    }

    public UUID create(UserId userId) {
        String query = "INSERT INTO session VALUES (?, ?, CURRENT_TIMESTAMP)";

        try (Connection conn = ds.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            UUID uuid = UUID.randomUUID();
            stmt.setString(1, uuid.toString());
            stmt.setInt(2, userId.getId());
            stmt.executeUpdate();
            return uuid;
        } catch (SQLException ex) {
            throw new CredentialsException("Unable to create session", ex);
        }
    }

    public void delete(UUID session) {
        String query = "DELETE FROM session WHERE id = ?";
        try (Connection conn = ds.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, session.toString());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error("Unable to delete session", ex);
        }
    }

    public UserId validate(UUID session) {
        String query = "SELECT user_id, created FROM session WHERE id = ?";
        try (Connection conn = ds.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);) {
            stmt.setString(1, session.toString());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("user_id");
                Timestamp timestamp = rs.getTimestamp("created");
                validateExpiration(timestamp);
                return UserId.of(userId);
            }
        } catch (SQLException ex) {
            throw new CredentialsException("Unable to validate session", ex);
        }

        throw new CredentialsException("Unable to validate session");
    }

    private void validateExpiration(Timestamp timestamp) {
        Instant sessionCreated = timestamp.toInstant();
        Instant now = Instant.now();
        if (sessionCreated.plus(SESSION_EXPIRATION).isBefore(now)) {
            throw new CredentialsException("Session has expired");
        }
    }
}
