# Define the base directory to search for Dockerfiles
$baseDirectory = "D:\Projects\JavaProjects\attendance-system"

# Recursively search for Dockerfiles without an extension and execute the docker build command
Get-ChildItem -Path $baseDirectory -Filter "Dockerfile*" -Recurse | ForEach-Object {
    if (-not $_.PSIsContainer) {
        $dockerfilePath = $_.DirectoryName
        $folderName = $_.Directory.Name
        $dockerBuildCommand = "docker build -t ${folderName}:${folderName} ."

        Write-Host "Running command for Dockerfile: $dockerBuildCommand"

        # Change current directory to the directory containing the Dockerfile
        Set-Location -Path $dockerfilePath

        # Execute the docker build command
        Invoke-Expression -Command $dockerBuildCommand

        # Return to the original directory
        Set-Location -Path $baseDirectory
    }
}
