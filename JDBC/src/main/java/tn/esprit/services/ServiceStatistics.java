package tn.esprit.services;

public class ServiceStatistics {
    private String doctorName;
    private double averageRating;
    private int complaintCount;

    // Constructor
    public ServiceStatistics(String doctorName, double averageRating, int complaintCount) {
        this.doctorName = doctorName;
        this.averageRating = averageRating;
        this.complaintCount = complaintCount;
    }

    // Getters and Setters
    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getComplaintCount() {
        return complaintCount;
    }

    public void setComplaintCount(int complaintCount) {
        this.complaintCount = complaintCount;
    }
}
