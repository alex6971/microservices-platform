@echo off
for /R %%s in (xxx*.txt,yyy*.dat,*.log, target) do (
del /q /s /a /f %%s
echo delete %%s
)
pause