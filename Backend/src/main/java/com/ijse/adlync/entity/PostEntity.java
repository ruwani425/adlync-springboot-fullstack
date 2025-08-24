package com.ijse.adlync.entity;

import com.ijse.adlync.entity.enums.PostEntityStatusEnum;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long post_id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PostEntityStatusEnum status;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "report_id")
    private ReportEntity report;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PaymentEntity> payments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReportEntity> reports = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advertisement_type_id")
    private Advertisement_typeEntity advertisement_type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private LocationEntity location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    private MessageEntity message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vehicle_id")
    private VehicleEntity vehicle;

    @OneToOne(mappedBy = "post")
    private AnimalEntity animal;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "property_id")
    private PropertyEntity property;

    @OneToOne(mappedBy = "post")
    private JobEntity job;

    @OneToOne(mappedBy = "post")
    private MobileEntity mobile;

    @OneToOne(mappedBy = "post")
    private ElectronicEntity electronic;

    @OneToOne(mappedBy = "post")
    private EducationEntity education;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "services_id")
    private ServicesEntity services;

    @OneToOne(mappedBy = "post")
    private AgricultureEntity agriculture;

    @OneToOne(mappedBy = "post")
    private Fashion_and_beautyEntity fashion_and_beauty;

    @OneToOne(mappedBy = "post")
    private KidsEntity kids;

    @OneToOne(mappedBy = "post")
    private EntertaintmentEntity entertaintment;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sport_id")
    private SportEntity sport;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "work_over_seas_id")
    private Work_over_seasEntity work_over_seas;

    @OneToOne(mappedBy = "post")
    private Home_and_gardenEntity home_and_garden;

    @OneToOne(mappedBy = "post")
    private EssentialsEntity essentials;

    public Long getPost_id() {
        return post_id;
    }

    public void setPost_id(Long post_id) {
        this.post_id = post_id;
    }

    public PostEntityStatusEnum getStatus() {
        return status;
    }

    public void setStatus(PostEntityStatusEnum status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public ReportEntity getReport() {
        return report;
    }

    public void setReport(ReportEntity report) {
        this.report = report;
    }

    public List<PaymentEntity> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentEntity> payments) {
        this.payments = payments;
    }

    public void addPayment(PaymentEntity payment) {
        this.payments.add(payment);
    }

    public void removePayment(PaymentEntity payment) {
        this.payments.remove(payment);
    }

    public List<ReportEntity> getReports() {
        return reports;
    }

    public void setReports(List<ReportEntity> reports) {
        this.reports = reports;
    }

    public void addReport(ReportEntity report) {
        this.reports.add(report);
    }

    public void removeReport(ReportEntity report) {
        this.reports.remove(report);
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public Advertisement_typeEntity getAdvertisement_type() {
        return advertisement_type;
    }

    public void setAdvertisement_type(Advertisement_typeEntity advertisement_type) {
        this.advertisement_type = advertisement_type;
    }

    public LocationEntity getLocation() {
        return location;
    }

    public void setLocation(LocationEntity location) {
        this.location = location;
    }

    public MessageEntity getMessage() {
        return message;
    }

    public void setMessage(MessageEntity message) {
        this.message = message;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public VehicleEntity getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleEntity vehicle) {
        this.vehicle = vehicle;
    }

    public AnimalEntity getAnimal() {
        return animal;
    }

    public void setAnimal(AnimalEntity animal) {
        this.animal = animal;
    }

    public PropertyEntity getProperty() {
        return property;
    }

    public void setProperty(PropertyEntity property) {
        this.property = property;
    }

    public JobEntity getJob() {
        return job;
    }

    public void setJob(JobEntity job) {
        this.job = job;
    }

    public MobileEntity getMobile() {
        return mobile;
    }

    public void setMobile(MobileEntity mobile) {
        this.mobile = mobile;
    }

    public ElectronicEntity getElectronic() {
        return electronic;
    }

    public void setElectronic(ElectronicEntity electronic) {
        this.electronic = electronic;
    }

    public EducationEntity getEducation() {
        return education;
    }

    public void setEducation(EducationEntity education) {
        this.education = education;
    }

    public ServicesEntity getServices() {
        return services;
    }

    public void setServices(ServicesEntity services) {
        this.services = services;
    }

    public AgricultureEntity getAgriculture() {
        return agriculture;
    }

    public void setAgriculture(AgricultureEntity agriculture) {
        this.agriculture = agriculture;
    }

    public Fashion_and_beautyEntity getFashion_and_beauty() {
        return fashion_and_beauty;
    }

    public void setFashion_and_beauty(Fashion_and_beautyEntity fashion_and_beauty) {
        this.fashion_and_beauty = fashion_and_beauty;
    }

    public KidsEntity getKids() {
        return kids;
    }

    public void setKids(KidsEntity kids) {
        this.kids = kids;
    }

    public EntertaintmentEntity getEntertaintment() {
        return entertaintment;
    }

    public void setEntertaintment(EntertaintmentEntity entertaintment) {
        this.entertaintment = entertaintment;
    }

    public SportEntity getSport() {
        return sport;
    }

    public void setSport(SportEntity sport) {
        this.sport = sport;
    }

    public Work_over_seasEntity getWork_over_seas() {
        return work_over_seas;
    }

    public void setWork_over_seas(Work_over_seasEntity work_over_seas) {
        this.work_over_seas = work_over_seas;
    }

    public Home_and_gardenEntity getHome_and_garden() {
        return home_and_garden;
    }

    public void setHome_and_garden(Home_and_gardenEntity home_and_garden) {
        this.home_and_garden = home_and_garden;
    }

    public EssentialsEntity getEssentials() {
        return essentials;
    }

    public void setEssentials(EssentialsEntity essentials) {
        this.essentials = essentials;
    }

}
