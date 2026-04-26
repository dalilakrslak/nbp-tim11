package com.nbp.cinemaapp.service;

import com.nbp.cinemaapp.entity.Location;
import com.nbp.cinemaapp.entity.Screening;
import com.nbp.cinemaapp.entity.Seat;
import com.nbp.cinemaapp.entity.Ticket;
import com.nbp.cinemaapp.entity.Venue;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class TicketPdfService {

    private static final DateTimeFormatter SCREENING_TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public byte[] generateTicketPdf(final Ticket ticket, final List<Seat> seats) {
        final List<String> lines = buildLines(ticket, seats);
        final String contentStream = buildContentStream(lines);

        return buildPdf(contentStream);
    }

    private List<String> buildLines(final Ticket ticket, final List<Seat> seats) {
        final Screening screening = ticket.getScreening();
        final Venue venue = screening.getHall().getVenue();
        final Location location = venue.getLocation();

        final String seatCodes = seats.stream()
                .sorted(Comparator.comparing(Seat::getSeatCode))
                .map(Seat::getSeatCode)
                .reduce((left, right) -> left + ", " + right)
                .orElse("N/A");

        final List<String> lines = new ArrayList<>();
        lines.add("Cinema Ticket");
        lines.add("");
        lines.add("Ticket ID: " + ticket.getId());
        lines.add("Movie: " + screening.getMovie().getTitle());
        lines.add("Date and time: " + screening.getStartTime().format(SCREENING_TIME_FORMAT));
        lines.add("Venue: " + venue.getName());
        lines.add("Address: " + venue.getStreet() + formatLocation(location));
        lines.add("Hall: " + screening.getHall().getName());
        lines.add("Seats: " + seatCodes);
        lines.add("Price: " + ticket.getPrice());
        lines.add("Status: " + ticket.getStatus().name());
        lines.add("Booked on: " + ticket.getBookingDate());
        return lines;
    }

    private String formatLocation(final Location location) {
        if (location == null) {
            return "";
        }

        return ", " + location.getCity() + ", " + location.getCountry();
    }

    private String buildContentStream(final List<String> lines) {
        final StringBuilder builder = new StringBuilder();
        builder.append("BT\n");
        builder.append("/F1 20 Tf\n");
        builder.append("50 790 Td\n");

        boolean firstLine = true;
        for (final String line : lines) {
            if (!firstLine) {
                builder.append("0 -24 Td\n");
            }

            builder.append("(")
                    .append(escapePdfText(line))
                    .append(") Tj\n");

            firstLine = false;
        }

        builder.append("ET\n");
        return builder.toString();
    }

    private byte[] buildPdf(final String contentStream) {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final List<Integer> offsets = new ArrayList<>();

        write(outputStream, "%PDF-1.4\n");

        offsets.add(outputStream.size());
        write(outputStream, "1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n");

        offsets.add(outputStream.size());
        write(outputStream, "2 0 obj\n<< /Type /Pages /Kids [3 0 R] /Count 1 >>\nendobj\n");

        offsets.add(outputStream.size());
        write(outputStream,
                "3 0 obj\n<< /Type /Page /Parent 2 0 R /MediaBox [0 0 595 842] " +
                        "/Resources << /Font << /F1 4 0 R >> >> /Contents 5 0 R >>\nendobj\n");

        offsets.add(outputStream.size());
        write(outputStream, "4 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>\nendobj\n");

        offsets.add(outputStream.size());
        final byte[] contentBytes = contentStream.getBytes(StandardCharsets.US_ASCII);
        write(outputStream, "5 0 obj\n<< /Length " + contentBytes.length + " >>\nstream\n");
        outputStream.writeBytes(contentBytes);
        write(outputStream, "endstream\nendobj\n");

        final int xrefStart = outputStream.size();
        write(outputStream, "xref\n0 6\n");
        write(outputStream, "0000000000 65535 f \n");
        for (final Integer offset : offsets) {
            write(outputStream, String.format("%010d 00000 n %n", offset));
        }

        write(outputStream,
                "trailer\n<< /Size 6 /Root 1 0 R >>\nstartxref\n" +
                        xrefStart +
                        "\n%%EOF");

        return outputStream.toByteArray();
    }

    private void write(final ByteArrayOutputStream outputStream, final String value) {
        outputStream.writeBytes(value.getBytes(StandardCharsets.US_ASCII));
    }

    private String escapePdfText(final String value) {
        return value
                .replace("\\", "\\\\")
                .replace("(", "\\(")
                .replace(")", "\\)");
    }
}
