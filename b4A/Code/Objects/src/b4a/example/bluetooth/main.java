package b4a.example.bluetooth;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = true;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mostCurrent = this;
		if (processBA == null) {
			processBA = new BA(this.getApplicationContext(), null, null, "b4a.example.bluetooth", "b4a.example.bluetooth.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
				p.finish();
			}
		}
        processBA.setActivityPaused(true);
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(processBA, wl, false))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "b4a.example.bluetooth", "b4a.example.bluetooth.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.example.bluetooth.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEventFromUI(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return main.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null)
            return;
        if (this != mostCurrent)
			return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        if (mostCurrent != null)
            processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        processBA.setActivityPaused(true);
        mostCurrent = null;
        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
            main mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (main) Resume **");
            if (mc != mostCurrent)
                return;
		    processBA.raiseEvent(mc._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        for (int i = 0;i < permissions.length;i++) {
            Object[] o = new Object[] {permissions[i], grantResults[i] == 0};
            processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
        }
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4a.objects.Serial.BluetoothAdmin _admin = null;
public static byte _nombrebt = (byte)0;
public static anywheresoftware.b4a.objects.collections.List _listname = null;
public static anywheresoftware.b4a.objects.collections.List _listmac = null;
public static anywheresoftware.b4a.objects.Serial _serial1 = null;
public static anywheresoftware.b4a.randomaccessfile.AsyncStreams _flux = null;
public static boolean _complet = false;
public static boolean _start = false;
public static anywheresoftware.b4a.keywords.StringBuilderWrapper _message = null;
public static byte[] _donnee = null;
public static String _message_envoie = "";
public static anywheresoftware.b4a.agraham.byteconverter.ByteConverter _bc = null;
public anywheresoftware.b4a.objects.ButtonWrapper _buttonsearch = null;
public anywheresoftware.b4a.objects.LabelWrapper _labelrecu = null;
public anywheresoftware.b4a.objects.SeekBarWrapper _seeksend = null;
public anywheresoftware.b4a.objects.LabelWrapper _labelseek = null;
public anywheresoftware.b4a.objects.ButtonWrapper _buttonmessage = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittextmessage = null;
public anywheresoftware.b4a.objects.LabelWrapper _labelmessage = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button1 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button2 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button3 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button4 = null;
public anywheresoftware.b4a.objects.LabelWrapper _labelrecucapteur = null;
public b4a.example.bluetooth.starter _starter = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 51;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 53;BA.debugLine="Activity.LoadLayout(\"Layout1\")";
mostCurrent._activity.LoadLayout("Layout1",mostCurrent.activityBA);
 //BA.debugLineNum = 54;BA.debugLine="admin.Initialize(\"admin\")";
_admin.Initialize(processBA,"admin");
 //BA.debugLineNum = 55;BA.debugLine="serial1.Initialize(\"serial1\")";
_serial1.Initialize("serial1");
 //BA.debugLineNum = 56;BA.debugLine="complet=False";
_complet = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 57;BA.debugLine="start=False";
_start = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 58;BA.debugLine="message.Initialize";
_message.Initialize();
 //BA.debugLineNum = 59;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 65;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 67;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 61;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 63;BA.debugLine="End Sub";
return "";
}
public static String  _admin_devicefound(String _name,String _macaddress) throws Exception{
 //BA.debugLineNum = 80;BA.debugLine="Sub Admin_DeviceFound (Name As String, MacAddress";
 //BA.debugLineNum = 81;BA.debugLine="nombreBT=nombreBT+1";
_nombrebt = (byte) (_nombrebt+1);
 //BA.debugLineNum = 82;BA.debugLine="listName.Add(Name)";
_listname.Add((Object)(_name));
 //BA.debugLineNum = 83;BA.debugLine="listMac.Add(MacAddress)";
_listmac.Add((Object)(_macaddress));
 //BA.debugLineNum = 84;BA.debugLine="End Sub";
return "";
}
public static String  _admin_discoveryfinished() throws Exception{
int _choix = 0;
 //BA.debugLineNum = 86;BA.debugLine="Sub Admin_DiscoveryFinished";
 //BA.debugLineNum = 87;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 88;BA.debugLine="Msgbox(\"Nombre de périphériques trouvés \"&nombreB";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Nombre de périphériques trouvés "+BA.NumberToString(_nombrebt)),BA.ObjectToCharSequence("Fin de la recherche"),mostCurrent.activityBA);
 //BA.debugLineNum = 89;BA.debugLine="Dim choix As Int";
_choix = 0;
 //BA.debugLineNum = 90;BA.debugLine="choix=InputList(listName,\"Choisissez un périphéri";
_choix = anywheresoftware.b4a.keywords.Common.InputList(_listname,BA.ObjectToCharSequence("Choisissez un périphérique"),(int) (-1),mostCurrent.activityBA);
 //BA.debugLineNum = 91;BA.debugLine="ProgressDialogShow(\"Connexion au péripérique : \"";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,BA.ObjectToCharSequence("Connexion au péripérique : "+BA.ObjectToString(_listname.Get(_choix))));
 //BA.debugLineNum = 92;BA.debugLine="serial1.Connect(listMac.Get(choix))";
_serial1.Connect(processBA,BA.ObjectToString(_listmac.Get(_choix)));
 //BA.debugLineNum = 94;BA.debugLine="End Sub";
return "";
}
public static String  _button1_click() throws Exception{
 //BA.debugLineNum = 138;BA.debugLine="Sub Button1_Click";
 //BA.debugLineNum = 139;BA.debugLine="message_envoie=\"Button1\"";
_message_envoie = "Button1";
 //BA.debugLineNum = 140;BA.debugLine="Donnee=bc.StringToBytes(message_envoie,\"ASCII\")";
_donnee = _bc.StringToBytes(_message_envoie,"ASCII");
 //BA.debugLineNum = 141;BA.debugLine="Flux.Write(Donnee)";
_flux.Write(_donnee);
 //BA.debugLineNum = 142;BA.debugLine="End Sub";
return "";
}
public static String  _button2_click() throws Exception{
 //BA.debugLineNum = 132;BA.debugLine="Sub Button2_Click";
 //BA.debugLineNum = 133;BA.debugLine="message_envoie=\"Button2\"";
_message_envoie = "Button2";
 //BA.debugLineNum = 134;BA.debugLine="Donnee=bc.StringToBytes(message_envoie,\"ASCII\")";
_donnee = _bc.StringToBytes(_message_envoie,"ASCII");
 //BA.debugLineNum = 135;BA.debugLine="Flux.Write(Donnee)";
_flux.Write(_donnee);
 //BA.debugLineNum = 136;BA.debugLine="End Sub";
return "";
}
public static String  _button3_click() throws Exception{
 //BA.debugLineNum = 126;BA.debugLine="Sub Button3_Click";
 //BA.debugLineNum = 127;BA.debugLine="message_envoie=\"Button3\"";
_message_envoie = "Button3";
 //BA.debugLineNum = 128;BA.debugLine="Donnee=bc.StringToBytes(message_envoie,\"ASCII\")";
_donnee = _bc.StringToBytes(_message_envoie,"ASCII");
 //BA.debugLineNum = 129;BA.debugLine="Flux.Write(Donnee)";
_flux.Write(_donnee);
 //BA.debugLineNum = 130;BA.debugLine="End Sub";
return "";
}
public static String  _button4_click() throws Exception{
 //BA.debugLineNum = 120;BA.debugLine="Sub Button4_Click";
 //BA.debugLineNum = 121;BA.debugLine="message_envoie=\"Button4\"";
_message_envoie = "Button4";
 //BA.debugLineNum = 122;BA.debugLine="Donnee=bc.StringToBytes(message_envoie,\"ASCII\")";
_donnee = _bc.StringToBytes(_message_envoie,"ASCII");
 //BA.debugLineNum = 123;BA.debugLine="Flux.Write(Donnee)";
_flux.Write(_donnee);
 //BA.debugLineNum = 124;BA.debugLine="End Sub";
return "";
}
public static String  _buttonmessage_click() throws Exception{
 //BA.debugLineNum = 112;BA.debugLine="Sub ButtonMessage_Click";
 //BA.debugLineNum = 113;BA.debugLine="message_envoie=EditTextMessage.Text";
_message_envoie = mostCurrent._edittextmessage.getText();
 //BA.debugLineNum = 114;BA.debugLine="LabelMessage.Text=message_envoie";
mostCurrent._labelmessage.setText(BA.ObjectToCharSequence(_message_envoie));
 //BA.debugLineNum = 115;BA.debugLine="Donnee=bc.StringToBytes(message_envoie,\"ASCII\")";
_donnee = _bc.StringToBytes(_message_envoie,"ASCII");
 //BA.debugLineNum = 116;BA.debugLine="Flux.Write(Donnee)";
_flux.Write(_donnee);
 //BA.debugLineNum = 118;BA.debugLine="End Sub";
return "";
}
public static String  _buttonrecucapteur_click() throws Exception{
 //BA.debugLineNum = 144;BA.debugLine="Sub ButtonRecuCapteur_Click";
 //BA.debugLineNum = 145;BA.debugLine="message_envoie=\"ButtonRecuCapteur\"";
_message_envoie = "ButtonRecuCapteur";
 //BA.debugLineNum = 146;BA.debugLine="Donnee=bc.StringToBytes(message_envoie,\"ASCII\")";
_donnee = _bc.StringToBytes(_message_envoie,"ASCII");
 //BA.debugLineNum = 147;BA.debugLine="Flux.Write(Donnee)";
_flux.Write(_donnee);
 //BA.debugLineNum = 148;BA.debugLine="End Sub";
return "";
}
public static String  _buttonsearch_click() throws Exception{
 //BA.debugLineNum = 70;BA.debugLine="Sub ButtonSearch_Click";
 //BA.debugLineNum = 72;BA.debugLine="admin.StartDiscovery";
_admin.StartDiscovery();
 //BA.debugLineNum = 74;BA.debugLine="ProgressDialogShow(\"Recherche de périphériques Bl";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,BA.ObjectToCharSequence("Recherche de périphériques Bluetooth..."));
 //BA.debugLineNum = 76;BA.debugLine="listName.Initialize";
_listname.Initialize();
 //BA.debugLineNum = 77;BA.debugLine="listMac.Initialize";
_listmac.Initialize();
 //BA.debugLineNum = 78;BA.debugLine="End Sub";
return "";
}
public static String  _flux_newdata(byte[] _buffer) throws Exception{
String _lettre = "";
int _i = 0;
 //BA.debugLineNum = 150;BA.debugLine="Sub Flux_NewData (Buffer() As Byte)";
 //BA.debugLineNum = 152;BA.debugLine="Dim lettre As String";
_lettre = "";
 //BA.debugLineNum = 154;BA.debugLine="For i=0 To Buffer.Length-1";
{
final int step2 = 1;
final int limit2 = (int) (_buffer.length-1);
_i = (int) (0) ;
for (;_i <= limit2 ;_i = _i + step2 ) {
 //BA.debugLineNum = 155;BA.debugLine="lettre=BytesToString(Buffer,i,1,\"ASCII\")";
_lettre = anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,_i,(int) (1),"ASCII");
 //BA.debugLineNum = 156;BA.debugLine="If (lettre.EqualsIgnoreCase(\"&\")==True) Then st";
if ((_lettre.equalsIgnoreCase("&")==anywheresoftware.b4a.keywords.Common.True)) { 
_start = anywheresoftware.b4a.keywords.Common.True;};
 //BA.debugLineNum = 158;BA.debugLine="If (lettre.EqualsIgnoreCase(\"%\")==True And star";
if ((_lettre.equalsIgnoreCase("%")==anywheresoftware.b4a.keywords.Common.True && _start==anywheresoftware.b4a.keywords.Common.True)) { 
 //BA.debugLineNum = 159;BA.debugLine="complet=True";
_complet = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 160;BA.debugLine="Exit";
if (true) break;
 };
 //BA.debugLineNum = 162;BA.debugLine="If (start==True) Then message.Append(lettre)";
if ((_start==anywheresoftware.b4a.keywords.Common.True)) { 
_message.Append(_lettre);};
 }
};
 //BA.debugLineNum = 165;BA.debugLine="If complet==True Then";
if (_complet==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 166;BA.debugLine="message.Remove(0,1)";
_message.Remove((int) (0),(int) (1));
 //BA.debugLineNum = 167;BA.debugLine="LabelRecu.Text=message";
mostCurrent._labelrecu.setText(BA.ObjectToCharSequence(_message.getObject()));
 //BA.debugLineNum = 168;BA.debugLine="message.Remove(0,message.Length)";
_message.Remove((int) (0),_message.getLength());
 //BA.debugLineNum = 169;BA.debugLine="complet=False";
_complet = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 170;BA.debugLine="start=False";
_start = anywheresoftware.b4a.keywords.Common.False;
 };
 //BA.debugLineNum = 173;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 32;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 36;BA.debugLine="Private ButtonSearch As Button";
mostCurrent._buttonsearch = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 37;BA.debugLine="Private LabelRecu As Label";
mostCurrent._labelrecu = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 38;BA.debugLine="Private SeekSend As SeekBar";
mostCurrent._seeksend = new anywheresoftware.b4a.objects.SeekBarWrapper();
 //BA.debugLineNum = 40;BA.debugLine="Private LabelSeek As Label";
mostCurrent._labelseek = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Private ButtonMessage As Button";
mostCurrent._buttonmessage = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 42;BA.debugLine="Private EditTextMessage As EditText";
mostCurrent._edittextmessage = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 43;BA.debugLine="Private LabelMessage As Label";
mostCurrent._labelmessage = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 44;BA.debugLine="Private Button1 As Button";
mostCurrent._button1 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 45;BA.debugLine="Private Button2 As Button";
mostCurrent._button2 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 46;BA.debugLine="Private Button3 As Button";
mostCurrent._button3 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 47;BA.debugLine="Private Button4 As Button";
mostCurrent._button4 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 48;BA.debugLine="Private LabelRecuCapteur As Label";
mostCurrent._labelrecucapteur = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 49;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
starter._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 15;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 18;BA.debugLine="Dim admin As BluetoothAdmin";
_admin = new anywheresoftware.b4a.objects.Serial.BluetoothAdmin();
 //BA.debugLineNum = 19;BA.debugLine="Dim nombreBT As Byte";
_nombrebt = (byte)0;
 //BA.debugLineNum = 20;BA.debugLine="Dim listName As List";
_listname = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 21;BA.debugLine="Dim listMac As List";
_listmac = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 22;BA.debugLine="Dim serial1 As Serial";
_serial1 = new anywheresoftware.b4a.objects.Serial();
 //BA.debugLineNum = 23;BA.debugLine="Dim Flux As AsyncStreams";
_flux = new anywheresoftware.b4a.randomaccessfile.AsyncStreams();
 //BA.debugLineNum = 25;BA.debugLine="Dim complet,start As Boolean";
_complet = false;
_start = false;
 //BA.debugLineNum = 26;BA.debugLine="Dim message As StringBuilder";
_message = new anywheresoftware.b4a.keywords.StringBuilderWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Dim Donnee() As Byte";
_donnee = new byte[(int) (0)];
;
 //BA.debugLineNum = 28;BA.debugLine="Dim message_envoie As String";
_message_envoie = "";
 //BA.debugLineNum = 29;BA.debugLine="Dim bc As ByteConverter";
_bc = new anywheresoftware.b4a.agraham.byteconverter.ByteConverter();
 //BA.debugLineNum = 30;BA.debugLine="End Sub";
return "";
}
public static String  _seeksend_valuechanged(int _value,boolean _userchanged) throws Exception{
 //BA.debugLineNum = 103;BA.debugLine="Sub SeekSend_ValueChanged (Value As Int, UserChang";
 //BA.debugLineNum = 106;BA.debugLine="message_envoie=SeekSend.value";
_message_envoie = BA.NumberToString(mostCurrent._seeksend.getValue());
 //BA.debugLineNum = 107;BA.debugLine="Donnee=bc.StringToBytes(message_envoie,\"ASCII\")";
_donnee = _bc.StringToBytes(_message_envoie,"ASCII");
 //BA.debugLineNum = 108;BA.debugLine="Flux.Write(Donnee)";
_flux.Write(_donnee);
 //BA.debugLineNum = 109;BA.debugLine="LabelSeek.Text=message_envoie";
mostCurrent._labelseek.setText(BA.ObjectToCharSequence(_message_envoie));
 //BA.debugLineNum = 110;BA.debugLine="End Sub";
return "";
}
public static String  _serial1_connected(boolean _success) throws Exception{
 //BA.debugLineNum = 96;BA.debugLine="Sub Serial1_Connected (Success As Boolean)";
 //BA.debugLineNum = 97;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 98;BA.debugLine="Msgbox(\"Connecté...\",\"\")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Connecté..."),BA.ObjectToCharSequence(""),mostCurrent.activityBA);
 //BA.debugLineNum = 99;BA.debugLine="Flux.Initialize(serial1.InputStream,serial1.Outpu";
_flux.Initialize(processBA,_serial1.getInputStream(),_serial1.getOutputStream(),"Flux");
 //BA.debugLineNum = 100;BA.debugLine="End Sub";
return "";
}
}
