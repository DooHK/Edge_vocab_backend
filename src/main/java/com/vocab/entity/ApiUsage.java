package com.vocab.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "api_usage", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "month_year"})
})
public class ApiUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "month_year", nullable = false)
    private String monthYear;

    @Column(name = "char_count")
    private int charCount = 0;

    @Column(name = "request_count")
    private int requestCount = 0;

    public ApiUsage() {}

    public ApiUsage(User user, String monthYear) {
        this.user = user;
        this.monthYear = monthYear;
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public String getMonthYear() { return monthYear; }
    public int getCharCount() { return charCount; }
    public int getRequestCount() { return requestCount; }

    public void addUsage(int chars) {
        this.charCount += chars;
        this.requestCount++;
    }
}
