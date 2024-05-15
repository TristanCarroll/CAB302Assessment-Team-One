package com.example.byebyeboxeyes;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.Guid.GUID;
import com.sun.jna.platform.win32.WinDef.*;
import com.sun.jna.win32.W32APIOptions;

import java.util.List;
import java.util.Arrays;

@SuppressWarnings("SpellCheckingInspection")
public class WinApi {
    static public class Shell32 {
        static {
            Native.register(NativeLibrary.getInstance("shell32", W32APIOptions.DEFAULT_OPTIONS));
        }

        // Mapping of Shell_NotifyIconA
        // https://learn.microsoft.com/en-us/windows/win32/api/shellapi/nf-shellapi-shell_notifyicona
        static public final int NIM_ADD = 0x0;
        static public final int NIM_MODIFY = 0x1;
        static public final int NIM_DELETE = 0x2;
        static public final int NIM_SETFOCUS = 0x3;
        static public final int NIM_SETVERSION = 0x4;

        static public native boolean Shell_NotifyIcon (int dwMessage, NOTIFYICONDATA lpData);
    }

    // Mapping of NOTIFYICONDATAA
    // https://learn.microsoft.com/en-us/windows/win32/api/shellapi/ns-shellapi-notifyicondataa
    static public class NOTIFYICONDATA extends Structure {
        static public final int NIF_MESSAGE = 0x1;
        static public final int NIF_ICON = 0x2;
        static public final int NIF_TIP = 0x4;
        static public final int NIF_STATE = 0x8;
        static public final int NIF_INFO = 0x10;
        static public final int NIF_GUID = 0x20;
        static public final int NIF_REALTIME = 0x40;
        static public final int NIF_SHOWTIP = 0x80;

        static public final int NIS_HIDDEN = 0x1;
        static public final int NIS_SHAREDICON = 0x1;

        static public final int NIIF_NONE = 0x0;
        static public final int NIIF_INFO = 0x1;
        static public final int NIIF_WARNING = 0x2;
        static public final int NIIF_ERROR = 0x3;
        static public final int NIIF_USER = 0x4;
        static public final int NIIF_NOSOUND = 0x10;
        static public final int NIIF_LARGE_ICON = 0x20;
        static public final int NIIF_RESPECT_QUIET_TIME = 0x80;
        static public final int NIIF_ICON_MASK = 0xF;

        public int cbSize;
        public HWND hWnd;
        public int uID;
        public int uFlags;
        public int uCallbackMessage;
        public Pointer hIcon;
        public char[] szTip = new char[128];
        public int dwState;
        public int dwStateMask;
        public char[] szInfo = new char[256];
        public int uTimeoutOrVersion; // {int uTimeout; int uVersion;};
        public char[] szInfoTitle = new char[64];
        public int dwInfoFlags;
        public GUID guidItem;
        Pointer hBalloonIcon;

        {
            cbSize = size();
        }

        protected List<String> getFieldOrder () {
            return Arrays.asList(new String[] {"cbSize", "hWnd", "uID", "uFlags", "uCallbackMessage", "hIcon", "szTip", "dwState",
                    "dwStateMask", "szInfo", "uTimeoutOrVersion", "szInfoTitle", "dwInfoFlags", "guidItem", "hBalloonIcon"});
        }
    }
}

