QPercent := [25,32.5,40,47.5,55]
Gui, Add, Text, x75 y0, Imput your AttackSpeed:
Gui, Add, Text, x75 y70, Imput your Q level:
Gui, Add, Edit, x90 y40 h20 w70 vAs,
Gui, Add, Edit, x90 y90 h20 w70 vQLvl,
Gui, Add, Button, x90 y150 h20 w70 gcalculate, Kite!
Gui, Show, h200 w250, Kitear

return

calculate:
{
	GuiControlGet, As
	GuiControlGet, QLvl
	Time := (((1000)/As)/3)
	PercentToUse := QPercent [QLvl]
}

$space::
send {Space}
while GetKeyState("space","P"){
	Send {RButton}
	Sleep ((Time/3)/2)
	Send {RButton}
	Sleep ((Time/3)/2)
	Send {RButton}
	Sleep ((Time/3)/2)
	PixelSearch, OutputVarX, OutputVarY, 374, 75, 1314, 820, 0x1B269A, 1, Fast
	if(OutputVarX!=null or OutputVarY!=null){
		MouseGetPos X, Y
		MouseMove, OutputVarX+40, OutputVarY+75, 0
		Send {x}
		MouseMove, X, Y, 0
		Sleep Time
	}
	Send {RButton}
	Sleep ((Time/3)/2)
	Send {RButton}
	Sleep ((Time/3)/2)
	Send {RButton}
	Sleep ((Time/3)/2)
}
return

$q::
PixelSearch, OutputVarX, OutputVarY, 374, 75, 1314, 820, 0x1B269A, 1, Fast
MouseGetPos X, Y
MouseMove, OutputVarX+30, OutputVarY+70, 0
Send {q}
Time := (((1000)/(As+(((0.658*PercentToUse)/100))))/3)
SetTimer ,ChangeTimer, 3000
Send {x}
MouseMove, X, Y, 0
return

ChangeTimer:
	GuiControlGet, As
	Time := (((1000)/As)/3)
return

$w::
PixelSearch, OutputVarX, OutputVarY, 0, 0, 1960, 1080, 0x1B269A, 1, Fast
MouseGetPos X, Y
MouseMove, OutputVarX+30, OutputVarY+70, 0
Send {w}
MouseMove, X, Y, 0
return

$r::
PixelSearch, OutputVarX, OutputVarY, 0, 0, 1960, 1080, 0x1B269A, 1, Fast
MouseGetPos X, Y
MouseMove, OutputVarX+50, OutputVarY+100, 0
Send {r}
MouseMove, X, Y, 0
return

$f2::
Suspend
return

$f1::
ExitApp

GuiClose:
ExitApp