import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Comparator;

public class AppointmentUI {
    private final AppointmentManager manager;
    private JFrame frame;

    public AppointmentUI(AppointmentManager manager) {
        this.manager = manager;
        createMainWindow();
    }

    private void createMainWindow() {
        if (frame != null) {
            frame.getContentPane().removeAll();
        } else {
            frame = new JFrame("Appointment Manager");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
        }

        JPanel mainPanel = new JPanel(new BorderLayout());
        JLabel headerLabel = new JLabel("Welcome to the Appointment Manager!", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.BLUE);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        JButton addButton = new JButton("Add Appointment");
        JButton viewButton = new JButton("View Appointments");
        JButton exitButton = new JButton("Exit");
        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(exitButton);

        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.add(headerLabel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        frame.add(mainPanel);
        frame.revalidate();
        frame.repaint();

        addButton.addActionListener(e -> showAddAppointmentPanel());
        viewButton.addActionListener(e -> showViewAppointmentsPanel());
        exitButton.addActionListener(e -> System.exit(0));

        frame.setVisible(true);
    }

    private void showAddAppointmentPanel() {
        JPanel addPanel = new JPanel(new GridLayout(9, 2, 10, 10));
        JLabel instructions = new JLabel("<html><p style='color:green;'>Please provide the appointment details below. " +
                "Dates must be in the format <b>YYYY-MM-DD</b>. Example: <i>2024-12-31</i>.</p></html>");

        JLabel descriptionLabel = new JLabel("Description:");
        JTextField descriptionField = new JTextField();
        JLabel startDateLabel = new JLabel("Start Date (YYYY-MM-DD):");
        DatePicker startDatePicker = new DatePicker(new DatePickerSettings());
        JLabel endDateLabel = new JLabel("End Date (YYYY-MM-DD):");
        DatePicker endDatePicker = new DatePicker(new DatePickerSettings());

        JLabel typeLabel = new JLabel("Appointment Type:");
        JRadioButton onetimeRadio = new JRadioButton("One-time");
        JRadioButton dailyRadio = new JRadioButton("Daily");
        JRadioButton monthlyRadio = new JRadioButton("Monthly");
        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(onetimeRadio);
        typeGroup.add(dailyRadio);
        typeGroup.add(monthlyRadio);
        onetimeRadio.setSelected(true);

        JButton saveButton = new JButton("Save Appointment");
        JButton backButton = new JButton("Back to Main Menu");

        addPanel.add(instructions);
        addPanel.add(new JLabel());
        addPanel.add(descriptionLabel);
        addPanel.add(descriptionField);
        addPanel.add(startDateLabel);
        addPanel.add(startDatePicker);
        addPanel.add(endDateLabel);
        addPanel.add(endDatePicker);
        addPanel.add(typeLabel);
        JPanel typePanel = new JPanel();
        typePanel.add(onetimeRadio);
        typePanel.add(dailyRadio);
        typePanel.add(monthlyRadio);
        addPanel.add(typePanel);
        addPanel.add(saveButton);
        addPanel.add(backButton);

        frame.getContentPane().removeAll();
        frame.add(addPanel);
        frame.revalidate();
        frame.repaint();

        saveButton.addActionListener(e -> {
            try {
                String description = descriptionField.getText();
                LocalDate startDate = startDatePicker.getDate();
                LocalDate endDate = endDatePicker.getDate();

                if (description.isEmpty() || startDate == null || endDate == null) {
                    throw new IllegalArgumentException("All fields are required!");
                }
                if (startDate.isAfter(endDate)) {
                    throw new IllegalArgumentException("Start date must not be after end date!");
                }

                Appointment appointment;
                if (onetimeRadio.isSelected()) {
                    appointment = new OnetimeAppointment(description, startDate);
                } else if (dailyRadio.isSelected()) {
                    appointment = new DailyAppointment(description, startDate, endDate);
                } else {
                    appointment = new MonthlyAppointment(description, startDate, endDate);
                }

                manager.add(appointment);
                JOptionPane.showMessageDialog(frame, "Appointment added successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> createMainWindow());
    }

    private void showViewAppointmentsPanel() {
        JPanel viewPanel = new JPanel(new BorderLayout());
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Appointment appointment : manager.getAppointments()) {
            listModel.addElement(appointment.toString());
        }

        JList<String> appointmentList = new JList<>(listModel);
        appointmentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel sortFilterPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JButton sortButton = new JButton("Sort by Date");
        JButton filterButton = new JButton("Filter by Date");
        sortFilterPanel.add(sortButton);
        sortFilterPanel.add(filterButton);

        JPanel actionPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JButton deleteButton = new JButton("Delete");
        JButton updateButton = new JButton("Update");
        actionPanel.add(deleteButton);
        actionPanel.add(updateButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(actionPanel, BorderLayout.CENTER);
        JButton backButton = new JButton("Back to Main Menu");
        bottomPanel.add(backButton, BorderLayout.SOUTH);

        viewPanel.add(new JScrollPane(appointmentList), BorderLayout.CENTER);
        viewPanel.add(sortFilterPanel, BorderLayout.NORTH);
        viewPanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.getContentPane().removeAll();
        frame.add(viewPanel);
        frame.revalidate();
        frame.repaint();

        sortButton.addActionListener(e -> {
            listModel.clear();
            for (Appointment appointment : manager.getAppointmentsOn(null, Comparator.naturalOrder())) {
                listModel.addElement(appointment.toString());
            }
        });

        filterButton.addActionListener(e -> {
            String dateInput = JOptionPane.showInputDialog(frame, "Enter date to filter (YYYY-MM-DD):");
            try {
                LocalDate filterDate = LocalDate.parse(dateInput);
                listModel.clear();
                for (Appointment appointment : manager.getAppointmentsOn(filterDate, null)) {
                    listModel.addElement(appointment.toString());
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid date format. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            String selected = appointmentList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(frame, "Please select an appointment to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this appointment?\n\n" + selected, "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                manager.getAppointments().removeIf(app -> app.toString().equals(selected));
                listModel.removeElement(selected);
                JOptionPane.showMessageDialog(frame, "Appointment deleted successfully!");
            }
        });

        updateButton.addActionListener(e -> {
            String selected = appointmentList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(frame, "Please select an appointment to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Appointment selectedAppointment = manager.getAppointments().stream()
                    .filter(app -> app.toString().equals(selected))
                    .findFirst()
                    .orElse(null);

            if (selectedAppointment == null) {
                JOptionPane.showMessageDialog(frame, "The selected appointment could not be found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to update this appointment?\n\nCurrent Appointment:\n" + selected, "Confirm Update", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                showUpdateAppointmentPanel(selectedAppointment);
            }
        });

        backButton.addActionListener(e -> createMainWindow());
    }

    private void showUpdateAppointmentPanel(Appointment appointmentToEdit) {
        JPanel updatePanel = new JPanel(new GridLayout(9, 2, 10, 10));
        JLabel instructions = new JLabel("<html><p style='color:orange;'>Update the details of the appointment below.</p></html>");

        JLabel descriptionLabel = new JLabel("Description:");
        JTextField descriptionField = new JTextField(appointmentToEdit.getDescription());
        JLabel startDateLabel = new JLabel("Start Date (YYYY-MM-DD):");
        DatePicker startDatePicker = new DatePicker(new DatePickerSettings());
        startDatePicker.setDate(appointmentToEdit.getStartDate());
        JLabel endDateLabel = new JLabel("End Date (YYYY-MM-DD):");
        DatePicker endDatePicker = new DatePicker(new DatePickerSettings());
        if (appointmentToEdit instanceof DailyAppointment || appointmentToEdit instanceof MonthlyAppointment) {
            endDatePicker.setDate(appointmentToEdit.getEndDate());
        }

        JLabel typeLabel = new JLabel("Appointment Type:");
        JRadioButton onetimeRadio = new JRadioButton("One-time");
        JRadioButton dailyRadio = new JRadioButton("Daily");
        JRadioButton monthlyRadio = new JRadioButton("Monthly");
        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(onetimeRadio);
        typeGroup.add(dailyRadio);
        typeGroup.add(monthlyRadio);

        if (appointmentToEdit instanceof OnetimeAppointment) {
            onetimeRadio.setSelected(true);
        } else if (appointmentToEdit instanceof DailyAppointment) {
            dailyRadio.setSelected(true);
        } else if (appointmentToEdit instanceof MonthlyAppointment) {
            monthlyRadio.setSelected(true);
        }

        JButton saveButton = new JButton("Update Appointment");
        JButton backButton = new JButton("Back to Main Menu");

        updatePanel.add(instructions);
        updatePanel.add(new JLabel());
        updatePanel.add(descriptionLabel);
        updatePanel.add(descriptionField);
        updatePanel.add(startDateLabel);
        updatePanel.add(startDatePicker);
        updatePanel.add(endDateLabel);
        updatePanel.add(endDatePicker);
        updatePanel.add(typeLabel);
        JPanel typePanel = new JPanel();
        typePanel.add(onetimeRadio);
        typePanel.add(dailyRadio);
        typePanel.add(monthlyRadio);
        updatePanel.add(typePanel);
        updatePanel.add(saveButton);
        updatePanel.add(backButton);

        frame.getContentPane().removeAll();
        frame.add(updatePanel);
        frame.revalidate();
        frame.repaint();

        saveButton.addActionListener(e -> {
            try {
                String newDescription = descriptionField.getText();
                LocalDate newStartDate = startDatePicker.getDate();
                LocalDate newEndDate = endDatePicker.getDate();

                if (newDescription.isEmpty() || newStartDate == null || (dailyRadio.isSelected() || monthlyRadio.isSelected() && newEndDate == null)) {
                    throw new IllegalArgumentException("All fields are required!");
                }
                if (newStartDate.isAfter(newEndDate)) {
                    throw new IllegalArgumentException("Start date must not be after end date!");
                }

                Appointment newAppointment;
                if (onetimeRadio.isSelected()) {
                    newAppointment = new OnetimeAppointment(newDescription, newStartDate);
                } else if (dailyRadio.isSelected()) {
                    newAppointment = new DailyAppointment(newDescription, newStartDate, newEndDate);
                } else {
                    newAppointment = new MonthlyAppointment(newDescription, newStartDate, newEndDate);
                }

                manager.getAppointments().remove(appointmentToEdit);
                manager.add(newAppointment);

                JOptionPane.showMessageDialog(frame, String.format("Appointment updated successfully!\n\nOriginal Appointment:\n%s\n\nUpdated Appointment:\n%s",
                        appointmentToEdit, newAppointment));

                createMainWindow();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> createMainWindow());
    }

    public static void main(String[] args) {
        AppointmentManager manager = new AppointmentManager();
        new AppointmentUI(manager);
    }
}
