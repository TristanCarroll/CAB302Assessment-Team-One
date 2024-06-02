package com.example.byebyeboxeyes.controller;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

/**
 * Class for managing the application's system tray functionality
 */
public class TrayIconController {
    private TrayIcon trayIcon = null;

    /**
     * Search for existing tray icon on initialisation
     */
    public TrayIconController() {
        TrayIcon[] icons = SystemTray.getSystemTray().getTrayIcons();
        for (TrayIcon i : icons) {
            if (Objects.equals(i.getToolTip(), "Bye Bye Box Eyes")) {
                this.trayIcon = i;
            }
        }
    }

    /**
     * Get the tray icon
     * @return current tray icon. Will be null if it doesn't exist
     */
    public TrayIcon getTrayIcon() {
        return trayIcon;
    }

    /**
     * Registers a tray icon to the System Tray
     * @param trayIcon a TrayIcon to register
     * @return true if successful, false otherwise
     */
    public boolean setTrayIcon(TrayIcon trayIcon) {
        WinDef.HWND hWnd = User32.INSTANCE.FindWindow(null, "Bye Bye Box Eyes");
        trayIcon.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                User32.INSTANCE.ShowWindow(hWnd, WinUser.SW_RESTORE);
            }
        });
        trayIcon.setToolTip("Bye Bye Box Eyes");

        try {
            SystemTray.getSystemTray().add(trayIcon);
        } catch (AWTException e) {
            return false;
        }

        this.trayIcon = trayIcon;
        return true;
    }

    /**
     * Removes the currently registered tray icon from the System Tray
     */
    public void deleteTrayIcon() {
        if (!trayIconExists()) { return; }

        SystemTray.getSystemTray().remove(this.trayIcon);
        this.trayIcon = null;
    }

    /**
     * Check if tray icon exists
     * @return true if tray icon is not null, otherwise false
     */
    public boolean trayIconExists() {
        return this.trayIcon != null;
    }
}
