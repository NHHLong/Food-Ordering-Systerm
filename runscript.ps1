$scriptRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$backendDir = Join-Path $scriptRoot "backend-spring"
$frontendDir = Join-Path $scriptRoot "frontend-react"

$mvndPath = $null
if ($env:MVND_HOME) {
	$candidate = Join-Path $env:MVND_HOME "bin\mvnd.cmd"
	if (Test-Path $candidate) {
		$mvndPath = $candidate
	}
}

if (-not $mvndPath) {
	$mvndCommand = Get-Command mvnd -ErrorAction SilentlyContinue
	if ($mvndCommand) {
		$mvndPath = $mvndCommand.Source
	}
}

if (-not $mvndPath) {
	Write-Error "mvnd was not found. Install mvnd or set MVND_HOME before running this script."
	exit 1
}

Start-Process powershell -ArgumentList "-NoExit", "-Command", "Set-Location '$backendDir'; & '$mvndPath' spring-boot:run"
Start-Process powershell -ArgumentList "-NoExit", "-Command", "Set-Location '$frontendDir'; npm run dev"