#SingleInstance force
#Persistent
#include <AutoHotInterception>
SetBatchLines, -1

global AHI := new AutoHotInterception()
global mouseId := AHI.GetMouseId(0x093A, 0x2532)
global stdout := FileOpen("*", "w")
global LBDown := false
global short := false
global red := false
global Black := 0x9D705F
global Yellow := 0x00FFFF
global mouseDistance := 2
global mouseLoop := 0
AHI.SubscribeMouseButton(mouseId, 0, true, Func("LClick"), true)
AHI.SubscribeMouseButton(mouseId, 1, true, Func("RClick"), true)
return

moveDown(){
    Click, down
    if(short){
        while(LBDown = true){
            PixelSearch, OutputVarX, OutputVarY, 1300, 500, 2700, 1500, %Yellow%, 3, Fast
            if( OutputVarX != null){
                AHI.Instance.SendMouseMoveAbsolute(mouseId, OutputVarX+50, OutputVarY+40)
                sleep, 10
            }
        }
    }
    if(long){
        while(LBDown = true){
            PixelSearch, OutputVarX, OutputVarY, 1300, 500, 2700, 1500, %Yellow%, 3, Fast
            if( OutputVarX != null){
                AHI.Instance.SendMouseMoveAbsolute(mouseId, OutputVarX+30, OutputVarY+30)
                sleep, 10
            }
        }
    }
    if(!short and !long){
        while(LBDown = true){
            AHI.Instance.SendMouseMoveRelative(mouseId, 0, mouseDistance)
            mouseLoop += 15
            sleep, 1
            if(mouseLoop >= 400){
                mouseDistance := 5
            }
        }
    }
}

LClick(state) {
    if (state) {
        LBDown := true
        SetTimer, moveDown, -1
    }else
        LBDown := false
        Click, up
        mouseLoop := 0
        mouseDistance := 2
}

RClick(state) {
    if state{
        Click, down, Right
        Send {Space}
    }else{
        Click, up, Right
	}
}

$XButton1::
if(long){
    long := false
    stdout.Write("Long off`n")
    stdout.Read()
}else{
    long := true
    short := false
    stdout.Write("Long on`n")
    stdout.Read()
}
return

$XButton2::
if(short){
    short := false
    stdout.Write("Short off`n")
    stdout.Read()
}else{
    short := true
    long := false
    stdout.Write("Short on`n")
    stdout.Read()
}
return

$x::
Send {q}
Send {x}
return

$c::
While(GetKeyState("C", "P")){
    Send {c}
    Sleep 200
}
return

$v::
While(GetKeyState("V", "P")){
    Send {v}
    Sleep 200
}
return

$Space::
While(GetKeyState("Space", "P")){
    Send {Space}
    Sleep 50
}
return

$F3::
Reload
return

$F1::
ExitApp