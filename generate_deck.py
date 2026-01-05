
import re
import os

files = [
    ("Novartis_Learning_Plan_Slide.html", {"content-wrapper": "content-wrapper-cover"}),
    ("Novartis_Gap_Analysis_Slide.html", {"content-wrapper": "content-wrapper-gap"}),
    ("Novartis_Gap1_Slide.html", {}),
    ("Novartis_Gap2_Slide.html", {}),
    ("Novartis_Gap3_Slide.html", {}),
    ("Novartis_PreOnboarding_Slide.html", {}),
    ("Novartis_PostOnboarding_Slide.html", {}),
    ("Novartis_Summary_Slide.html", {"content-wrapper": "content-wrapper-summary"}),
]

base_path = "c:\\Practice\\learning"
output_path = os.path.join(base_path, "Novartis_Presentation_Deck.html")

all_css = ""
all_html = ""

for filename, replacements in files:
    file_path = os.path.join(base_path, filename)
    with open(file_path, "r", encoding="utf-8") as f:
        content = f.read()

    # Extract CSS
    css_match = re.search(r"<style>(.*?)</style>", content, re.DOTALL)
    if css_match:
        css = css_match.group(1)
        for old, new in replacements.items():
            css = css.replace(f".{old}", f".{new}")
        all_css += f"\n/* CSS from {filename} */\n{css}\n"

    # Extract HTML Body Content (slide-container)
    html_match = re.search(r"<body>(.*?)</body>", content, re.DOTALL)
    if html_match:
        html = html_match.group(1)
        # Find the slide-container div
        container_match = re.search(r'(<div class="slide-container">.*?</div>)\s*$', html, re.DOTALL)
        if container_match:
            slide_html = container_match.group(1)
        else:
            # Fallback if regex fails (e.g. extra whitespace), just take the body content but try to trim
            slide_html = html.strip()
        
        for old, new in replacements.items():
            slide_html = slide_html.replace(f'class="{old}"', f'class="{new}"')
            slide_html = slide_html.replace(f'class="{old} ', f'class="{new} ')
        
        all_html += f"\n<!-- Slide from {filename} -->\n{slide_html}\n"

final_html = f"""<!DOCTYPE html>
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
    body {{
        font-family: 'Noto Sans SC', sans-serif;
        background-color: #555;
        margin: 0;
        padding: 40px;
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 40px;
    }}
    
    .slide-container {{
        width: 1280px;
        height: 720px;
        background-color: #ffffff;
        position: relative;
        overflow: hidden;
        box-shadow: 0 0 20px rgba(0,0,0,0.5);
        flex-shrink: 0;
    }}

    @media print {{
        body {{
            background-color: white;
            padding: 0;
            margin: 0;
            display: block;
        }}
        .slide-container {{
            box-shadow: none;
            page-break-after: always;
            margin: 0;
            width: 100%;
            height: 100%;
        }}
    }}

    /* Extracted Styles */
    {all_css}
</style>
</head>
<body>
{all_html}
</body>
</html>
"""

with open(output_path, "w", encoding="utf-8") as f:
    f.write(final_html)

print(f"Successfully created {output_path}")
