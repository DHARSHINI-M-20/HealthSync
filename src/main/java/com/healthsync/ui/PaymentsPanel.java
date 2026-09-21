package com.healthsync.ui;

import com.healthsync.model.Payment;
import com.healthsync.service.BillingService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;

/** Payments view backed by BillingService. */
public class PaymentsPanel extends JPanel {

    private final AppContext context;
    private final JTextField invoiceField = new JTextField();
    private final DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Payment ID", "Invoice", "Amount", "Method", "Status"}, 0);
    private final JTable table = new JTable(tableModel);

    public PaymentsPanel(AppContext context) {
        this.context = context;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        JTextField amountField = new JTextField();
        JSpinner methodSpinner = new JSpinner(new SpinnerListModel(new Object[]{"CASH", "CARD", "INSURANCE"}));
        JButton payButton = new JButton("Process payment");
        JButton refreshButton = new JButton("Refresh");

        form.add(new JLabel("Invoice ID")); form.add(invoiceField);
        form.add(new JLabel("Amount")); form.add(amountField);
        form.add(new JLabel("Method")); form.add(methodSpinner);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actions.add(payButton); actions.add(refreshButton);

        JPanel north = new JPanel(new BorderLayout());
        north.add(form, BorderLayout.CENTER);
        north.add(actions, BorderLayout.SOUTH);

        table.setRowHeight(22);
        add(north, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        payButton.addActionListener(e -> {
            try {
                com.healthsync.model.Payment payment = context.billing.recordPayment(invoiceField.getText(),
                        new BigDecimal(amountField.getText()), (String) methodSpinner.getValue());
                JOptionPane.showMessageDialog(this, "Payment recorded: " + payment.getId());
                refresh();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
        refreshButton.addActionListener(e -> refresh());
        refresh();
    }

    private void refresh() {
        tableModel.setRowCount(0);
        String invoiceId = invoiceField.getText().trim();
        var payments = invoiceId.isBlank()
                ? context.billing.findAllInvoices().stream()
                .flatMap(invoice -> context.billing.findPayments(invoice.getId()).stream())
                .toList()
                : context.billing.findPayments(invoiceId);
        for (Payment payment : payments) {
            tableModel.addRow(new Object[]{
                    payment.getId(), payment.getInvoiceId(), payment.getAmount(), payment.getMethod(), payment.getStatus()
            });
        }
    }
}