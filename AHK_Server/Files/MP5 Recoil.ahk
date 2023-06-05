;MP5--Cañón Silenciador integrado subsónico--Cargador de 45 balas--Empuñadura trasera punteado--Culata Plegable FTAC--Láser de 1mW

#SingleInstance force
#Persistent
#include <AutoHotInterception>
SetBatchLines, -1

global AHI := new AutoHotInterception()
global mouseId := AHI.GetMouseId(0x093A, 0x2532)
global LBDown := false
global RBDown := false
global mouseY := 2
global mouseX := 0
global mouseLoop := 0
AHI.SubscribeMouseButton(mouseId, 0, true, Func("LClick"), true)
AHI.SubscribeMouseButton(mouseId, 1, true, Func("RClick"), true)
return

moveDown(){
    Click, down
    while(LBDown and RBDown){
        AHI.Instance.SendMouseMoveRelative(mouseId, mouseX, mouseY)
        mouseLoop += 15
        sleep, 1
        if(mouseLoop >= 340){
            mouseX := -1
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
        mouseY := 2
        mouseX := 0
}
RClick(state) {
    if state{
        RBDown := true
        Click, down, Right
    }else{
        RBDown := false
        Click, up, Right
	}
}

$F1::
ExitApp