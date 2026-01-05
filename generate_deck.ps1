
$files = @(
    "Novartis_Learning_Plan_Slide.html",
    "Novartis_Gap_Analysis_Slide.html",
    "Novartis_Gap1_Slide.html",
    "Novartis_Gap2_Slide.html",
    "Novartis_Gap3_Slide.html",
    "Novartis_PreOnboarding_Slide.html",
    "Novartis_PostOnboarding_Slide.html",
    "Novartis_Summary_Slide.html"
)

$basePath = "c:\Practice\learning"
$outputPath = Join-Path $basePath "Novartis_Presentation_Deck.html"

$allCss = ""
$allHtml = ""
$i = 0

foreach ($filename in $files) {
    $filePath = Join-Path $basePath $filename
    $content = Get-Content $filePath -Raw -Encoding UTF8
    $slideId = "slide-$i"

    # Extract CSS
    if ($content -match "(?s)<style>(.*?)</style>") {
        $css = $matches[1]
        
        # 1. Normalize body and container to a placeholder .SLIDEROOT
        $css = $css -replace "body\s*\{", ".SLIDEROOT {"
        $css = $css -replace "\.slide-container", ".SLIDEROOT"
        
        # 2. Prefix every selector with #slide-N
        # We split by '}' to get blocks, then process the selector part
        $blocks = $css -split "}"
        $newCss = ""
        
        foreach ($block in $blocks) {
            if ($block -match "^\s*$") { continue }
            
            # Split selector and body
            $parts = $block -split "{", 2
            if ($parts.Count -lt 2) { continue }
            
            $selectorPart = $parts[0]
            $rulePart = $parts[1]
            
            # Handle commas in selectors
            $selectors = $selectorPart -split ","
            $newSelectors = @()
            
            foreach ($sel in $selectors) {
                $sel = $sel.Trim()
                if ($sel -eq "") { continue }
                if ($sel.StartsWith("@")) { 
                    # Media queries or keyframes - leave as is (simplified handling)
                    $newSelectors += $sel
                } else {
                    # Prefix
                    $newSelectors += "#$slideId $sel"
                }
            }
            
            $finalSelector = $newSelectors -join ", "
            $newCss += "$finalSelector { $rulePart }`n"
        }
        
        # 3. Fix the .SLIDEROOT to be just the ID (remove the space)
        # Current state: #slide-0 .SLIDEROOT { ... }
        # Desired state: #slide-0 { ... }
        $newCss = $newCss -replace "#$slideId .SLIDEROOT", "#$slideId"
        
        $allCss += "`n/* CSS from $filename */`n$newCss`n"
    }

    # Extract HTML Body Content
    if ($content -match "(?s)<body>(.*?)</body>") {
        $htmlBody = $matches[1]
        # Try to find slide-container div
        if ($htmlBody -match "(?s)(<div class=""slide-container"">.*?</div>)\s*$") {
            $slideHtml = $matches[1]
        } else {
            $slideHtml = $htmlBody
        }

        # Add the ID to the container
        # We keep the class slide-container for generic styling if needed, but ID is key
        $slideHtml = $slideHtml -replace 'class="slide-container"', "id=""$slideId"" class=""slide-container"""
        
        $allHtml += "`n<!-- Slide from $filename -->`n$slideHtml`n"
    }
    $i++
}

$finalHtml = @"
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8"/>
<meta content="width=device-width, initial-scale=1.0" name="viewport"/>
<title>Novartis Presentation Deck</title>
<link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet"/>
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+SC:wght@300;400;500;700;900&amp;display=swap" rel="stylesheet"/>
<link href="https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@6.4.0/css/all.min.css" rel="stylesheet"/>
<style>
    /* Global Reset & Print Styles */
    body {
        font-family: 'Noto Sans SC', sans-serif;
        background-color: #555;
        margin: 0;
        padding: 40px;
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 40px;
        overflow-y: auto;
    }
    
    /* Common Slide Container Styles */
    .slide-container {
        width: 1280px;
        height: 720px;
        background-color: #ffffff;
        position: relative;
        overflow: hidden;
        box-shadow: 0 0 20px rgba(0,0,0,0.5);
        flex-shrink: 0;
    }

    @media print {
        body {
            background-color: white;
            padding: 0;
            margin: 0;
            display: block;
        }
        .slide-container {
            box-shadow: none;
            page-break-after: always;
            margin: 0;
            width: 100%;
            height: 100%;
        }
    }

    /* Extracted and Scoped Styles */
    $allCss
</style>
</head>
<body>
$allHtml
</body>
</html>
"@

$finalHtml | Set-Content $outputPath -Encoding UTF8
Write-Host "Successfully created $outputPath"
