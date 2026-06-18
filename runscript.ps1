$scriptRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$backendDir = Join-Path $scriptRoot "backend-spring"
$frontendDir = Join-Path $scriptRoot "frontend-react"

$knownMvndPaths = @(
	(Join-Path $env:MVND_HOME "bin\mvnd.cmd"),
	"D:\maven-mvnd-1.0.6-windows-amd64\bin\mvnd.cmd"
)

$mvndPath = $null

foreach ($candidate in $knownMvndPaths) {
	if ($candidate -and (Test-Path $candidate)) {
		$mvndPath = $candidate
		break
	}
}

if (-not $mvndPath) {
	$mvndCommand = Get-Command mvnd -ErrorAction SilentlyContinue
	if ($mvndCommand) {
		$mvndPath = $mvndCommand.Path
	}
}

if (-not $mvndPath) {
	Write-Error "mvnd was not found. Install mvnd or set MVND_HOME before running this script."
	exit 1
}

Start-Process powershell -ArgumentList "-NoExit", "-Command", "Set-Location -LiteralPath '$backendDir'; & '$mvndPath' spring-boot:run"
Start-Process powershell -ArgumentList "-NoExit", "-Command", "Set-Location -LiteralPath '$frontendDir'; npm.cmd run dev"
Start-Process "http://127.0.0.1:5173/"