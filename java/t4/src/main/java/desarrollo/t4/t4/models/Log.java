package desarrollo.t4.t4.models;

import jakarta.persistence.*;

@Entity
@Table(name = "logs")
public class Log {
    private @Id
    @SequenceGenerator(
            name = "log_seq",
            sequenceName = "log_sequence",
            allocationSize = 1
    ) @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "log_seq"
    ) Long id;

    private String message;

    public Log() {}

    public Log(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
