CREATE TABLE daily_ticket_report (
    id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
    report_date DATE NOT NULL UNIQUE,
    total_revenue NUMBER(12,2) NOT NULL,
    total_tickets NUMBER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
