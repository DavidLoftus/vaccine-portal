package howdo.vaccine.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private Date dateOfBirth;

    @NotBlank
    private String ppsNumber;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String emailAddress;

    @NotBlank
    private String nationality;

    @OneToMany(mappedBy = "user")
    @OrderBy("dose")
    private List<VaccineDose> doses;

    @OneToMany(mappedBy = "user")
    @OrderBy("appointmentTime")
    private List<Appointment> appointments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
