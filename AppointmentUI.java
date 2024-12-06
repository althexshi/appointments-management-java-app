// UI Design
package obj;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class AppointmentUI {

    private AppointmentManager manager;

    public AppointmentUI(AppointmentManager manager) {
        this.manager = manager;
        createMainWindow();
    }

    private void createMainWindow() {
        JFrame frame = new JFrame("Appointment Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JButton addButton = new JButton("Add Appointment");
        JButton displayButton = new JButton("Display Appointments");
        JButton exitButton = new JButton("Exit");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(displayButton);
        buttonPanel.add(exitButton);

        JLabel statusLabel = new JLabel("Welcome to the Appointment Manager!", JLabel.CENTER);

        mainPanel.add(statusLabel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(mainPanel);

        addButton.addActionListener(e -> showAddDialog(frame, null));
        displayButton.addActionListener(e -> showDisplayPanel(frame));
        exitButton.addActionListener(e -> System.exit(0));

        frame.setVisible(true);
    }

    private void showAddDialog(JFrame parent, Appointment existing) {
        JDialog dialog = new JDialog(parent, "Add Appointment", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new GridLayout(6, 2));

        JTextField descriptionField = new JTextField();
        DatePicker startDatePicker = new DatePicker(new DatePickerSettings());
        DatePicker endDatePicker = new DatePicker(new DatePickerSettings());

        JRadioButton onetimeRadio = new JRadioButton("Onetime");
        JRadioButton dailyRadio = new JRadioButton("Daily");
        JRadioButton monthlyRadio = new JRadioButton("Monthly");
        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(onetimeRadio);
        typeGroup.add(dailyRadio);
        typeGroup.add(monthlyRadio);
        onetimeRadio.setSelected(true);

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        dialog.add(new JLabel("Description:"));
        dialog.add(descriptionField);
        dialog.add(new JLabel("Start Date:"));
        dialog.add(startDatePicker);
        dialog.add(new JLabel("End Date:"));
        dialog.add(endDatePicker);
        dialog.add(new JLabel("Type:"));
        JPanel typePanel = new JPanel();
        typePanel.add(onetimeRadio);
        typePanel.add(dailyRadio);
        typePanel.add(monthlyRadio);
        dialog.add(typePanel);
        dialog.add(saveButton);
        dialog.add(cancelButton);

        saveButton.addActionListener(e -> {
            try {
                String description = descriptionField.getText();
                LocalDate startDate = startDatePicker.getDate();
                LocalDate endDate = endDatePicker.getDate();

                if (description.isEmpty() || startDate == null || endDate == null) {
                    throw new IllegalArgumentException("All fields must be filled!");
                }
                if (startDate.isAfter(endDate)) {
                    throw new IllegalArgumentException("Start date must not be after end date!");
                }

                Appointment appointment = null;
                if (onetimeRadio.isSelected()) {
                    appointment = new OnetimeAppointment(description, startDate);
                } else if (dailyRadio.isSelected()) {
                    appointment = new DailyAppointment(description, startDate, endDate);
                } else if (monthlyRadio.isSelected()) {
                    appointment = new MonthlyAppointment(description, startDate, endDate);
                }

                if (existing == null) {
                    manager.add(appointment);
                } else {
                    manager.update(existing, appointment);
                }

                JOptionPane.showMessageDialog(dialog, "Appointment saved!");
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void showDisplayPanel(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Display Appointments", true);
        dialog.setSize(600, 400);
        dialog.setLayout(new BorderLayout());

        JList<Appointment> appointmentList = new JList<>();
        DefaultListModel<Appointment> listModel = new DefaultListModel<>();
        for (Appointment appointment : manager.getAppointments()) {
            listModel.addElement(appointment);
        }
        appointmentList.setModel(listModel);

        JPanel controlPanel = new JPanel();
        JButton deleteButton = new JButton("Delete");
        JButton updateButton = new JButton("Update");
        controlPanel.add(deleteButton);
        controlPanel.add(updateButton);

        dialog.add(new JScrollPane(appointmentList), BorderLayout.CENTER);
        dialog.add(controlPanel, BorderLayout.SOUTH);

        deleteButton.addActionListener(e -> {
            Appointment selected = appointmentList.getSelectedValue();
            if (selected != null) {
                manager.delete(selected);
                listModel.removeElement(selected);
                JOptionPane.showMessageDialog(dialog, "Appointment deleted!");
            }
        });

        updateButton.addActionListener(e -> {
            Appointment selected = appointmentList.getSelectedValue();
            if (selected != null) {
                dialog.dispose();
                showAddDialog(parent, selected);
            }
        });

        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        AppointmentManager manager = new AppointmentManager();
        new AppointmentUI(manager);
    }
}