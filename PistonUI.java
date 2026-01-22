import javax.swing.*;
import java.awt.*;

public class PistonUI {
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}

        JFrame frame = new JFrame("Piston Launcher");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(400, 350));
        frame.getContentPane().setBackground(new Color(25, 25, 25));
        frame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel title = new JLabel("PISTON BRIDGE", SwingConstants.CENTER);
        title.setForeground(new Color(220, 220, 220));
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JTextField ipField = new JTextField("mc.hypixel.net");
        JTextField portField = new JTextField("25565");

        JButton startBtn = new JButton("ACTIVATE PISTON");
        startBtn.setBackground(new Color(45, 150, 45));
        startBtn.setForeground(Color.WHITE);
        startBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel status = new JLabel("Status: Ready", SwingConstants.CENTER);
        status.setForeground(Color.GRAY);

        startBtn.addActionListener(e -> {
            String ip = ipField.getText();
            int port = Integer.parseInt(portField.getText());

            // Run in thread to prevent UI freeze
            new Thread(() -> {
                try {
                    startBtn.setEnabled(false);
                    status.setText("Checking engine...");
                    Piston.checkAndDownloadGeyser();
                    
                    status.setText("Connecting to " + ip + "...");
                    Piston.startBridge(ip, port);
                    
                    status.setText("PISTON ACTIVE - Join 127.0.0.1");
                    status.setForeground(new Color(50, 205, 50));
                } catch (Exception ex) {
                    status.setText("Error: " + ex.getMessage());
                    status.setForeground(Color.RED);
                    startBtn.setEnabled(true);
                }
            }).start();
        });

        gbc.gridy = 0; frame.add(title, gbc);
        gbc.gridy = 1; frame.add(new JLabel("<html><font color='gray'>SERVER IP</font></html>"), gbc);
        gbc.gridy = 2; frame.add(ipField, gbc);
        gbc.gridy = 3; frame.add(new JLabel("<html><font color='gray'>PORT</font></html>"), gbc);
        gbc.gridy = 4; frame.add(portField, gbc);
        gbc.gridy = 5; frame.add(startBtn, gbc);
        gbc.gridy = 6; frame.add(status, gbc);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Kill bridge when window closes
        Runtime.getRuntime().addShutdownHook(new Thread(Piston::stopBridge));
    }
}