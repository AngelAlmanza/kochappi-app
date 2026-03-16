# Color Palette

Kochappi uses a palette built around **energy, discipline, and professionalism** — serving both intense training and coach administration workflows.

Core combination: **Vibrant Orange** (energy/action) + **Slate Blue-Gray** (trust/stability).

---

## Tokens (Material 3)

```json
{
  "theme_name": "Kochappi Professional Fitness",
  "light": {
    "primary": "#F97316",
    "onPrimary": "#FFFFFF",
    "primaryContainer": "#FFEDD5",
    "onPrimaryContainer": "#7C2D12",
    "secondary": "#334155",
    "onSecondary": "#FFFFFF",
    "secondaryContainer": "#E2E8F0",
    "onSecondaryContainer": "#0F172A",
    "tertiary": "#10B981",
    "onTertiary": "#FFFFFF",
    "tertiaryContainer": "#D1FAE5",
    "onTertiaryContainer": "#064E3B",
    "error": "#EF4444",
    "onError": "#FFFFFF",
    "background": "#F8FAFC",
    "onBackground": "#0F172A",
    "surface": "#FFFFFF",
    "onSurface": "#0F172A",
    "outline": "#94A3B8"
  },
  "dark": {
    "primary": "#FB923C",
    "onPrimary": "#431407",
    "primaryContainer": "#7C2D12",
    "onPrimaryContainer": "#FFEDD5",
    "secondary": "#94A3B8",
    "onSecondary": "#0F172A",
    "secondaryContainer": "#334155",
    "onSecondaryContainer": "#F1F5F9",
    "tertiary": "#34D399",
    "onTertiary": "#064E3B",
    "tertiaryContainer": "#065F46",
    "onTertiaryContainer": "#D1FAE5",
    "error": "#F87171",
    "onError": "#450A0A",
    "background": "#0F172A",
    "onBackground": "#F1F5F9",
    "surface": "#1E293B",
    "onSurface": "#F1F5F9",
    "outline": "#475569"
  }
}
```

---

## Color Roles

| Role | Hex (Light) | Hex (Dark) | Usage |
|---|---|---|---|
| Primary | `#F97316` | `#FB923C` | CTAs, "Start Workout", 1RM buttons |
| Secondary | `#334155` | `#94A3B8` | Trainer UI chrome, headers, structure |
| Tertiary | `#10B981` | `#34D399` | Progress charts, PRs, 100% completion badges |
| Error | `#EF4444` | `#F87171` | Validation errors, destructive actions |
| Background (dark) | — | `#0F172A` | Deep navy instead of pure black |

---

## Design Rationale

- **Primary Orange `#F97316`** — Signals motivation and physical effort. The color of action.
- **Secondary Slate `#334155`** — Adds the seriousness the trainer role requires. Robust, like iron plates.
- **Tertiary Emerald `#10B981`** — Reserved for success and progress: evolution charts, personal records, completed days.
- **Dark background `#0F172A`** — Deep navy instead of pure black. Reduces eye strain in gym lighting and looks premium on OLED screens.
