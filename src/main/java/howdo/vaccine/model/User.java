package howdo.vaccine.model;

import howdo.vaccine.enums.Nationality;
import org.hibernate.annotations.ColumnTransformer;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @ElementCollection
    private Set<String> authorities;

    @NotBlank
    @ColumnTransformer(
            read="AES_DECRYPT(UNHEX(password), UNHEX(SHA2('secret', 512)))",
            write="HEX(AES_ENCRYPT(?, UNHEX(SHA2('secret', 512))))"
    )
    private String password;


    @NotBlank
    @ColumnTransformer(
            read="AES_DECRYPT(UNHEX(first_name), UNHEX(SHA2('secret', 512)))",
            write="HEX(AES_ENCRYPT(?, UNHEX(SHA2('secret', 512))))"
    )
    private String firstName;

    @NotBlank
    @ColumnTransformer(
            read="AES_DECRYPT(UNHEX(last_name), UNHEX(SHA2('secret', 512)))",
            write="HEX(AES_ENCRYPT(?, UNHEX(SHA2('secret', 512))))"
    )
    private String lastName;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date dateOfBirth;

    @NotBlank
    @Column(unique = true)
    @ColumnTransformer(
            read="AES_DECRYPT(UNHEX(pps_number), UNHEX(SHA2('secret', 512)))",
            write="HEX(AES_ENCRYPT(?, UNHEX(SHA2('secret', 512))))"
    )
    private String ppsNumber;

    @NotBlank
    @ColumnTransformer(
            read="AES_DECRYPT(UNHEX(phone_number), UNHEX(SHA2('secret', 512)))",
            write="HEX(AES_ENCRYPT(?, UNHEX(SHA2('secret', 512))))"
    )
    private String phoneNumber;

    @Email
    @NotBlank
    @ColumnTransformer(
            read="AES_DECRYPT(UNHEX(phone_number), UNHEX(SHA2('secret', 512)))",
            write="HEX(AES_ENCRYPT(?, UNHEX(SHA2('secret', 512))))"
    )
    private String emailAddress;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Nationality nationality;

    @OneToMany(mappedBy = "user")
    @OrderBy("dose")
    private List<VaccineDose> doses;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    @OrderBy("appointmentTime")
    private List<Appointment> appointments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPpsNumber() {
        return ppsNumber;
    }

    public void setPpsNumber(String ppsNumber) {
        this.ppsNumber = ppsNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Nationality getNationality() {
        return nationality;
    }

    public void setNationality(Nationality nationality) {
        this.nationality = nationality;
    }

    public List<VaccineDose> getDoses() {
        return doses;
    }

    public void setDoses(List<VaccineDose> doses) {
        this.doses = doses;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }
}
