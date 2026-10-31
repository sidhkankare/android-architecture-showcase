# 🔒 Security Guide - Protect Your Code & Credentials

## ⚠️ IMMEDIATE ACTION REQUIRED

You previously shared a GitHub Personal Access Token in plain text. **This is extremely dangerous.**

### Step 1: Revoke the Token (DO THIS NOW)

1. Go to: https://github.com/settings/tokens
2. Find any token that looks like: `ghp_XXXXXXXXXXXXXXXXXXXX`
3. Click "Delete" or "Revoke"
4. Confirm deletion

**Why this matters:** Anyone with that token can:
- Delete all your repositories
- Modify your code
- Access private projects
- Impersonate you on GitHub

---

## 🛡️ Safe Authentication Methods

### Method 1: SSH Keys (RECOMMENDED)

#### Why SSH is Better:
- ✅ More secure than passwords/tokens
- ✅ No need to type credentials
- ✅ Can't be accidentally exposed
- ✅ Industry standard

#### Setup SSH (5 minutes):

```bash
# 1. Generate SSH key
ssh-keygen -t ed25519 -C "your.email@example.com"
# Press Enter 3 times (accept defaults)

# 2. Start SSH agent
eval "$(ssh-agent -s)"

# 3. Add your key
ssh-add ~/.ssh/id_ed25519

# 4. Copy your PUBLIC key (safe to share)
cat ~/.ssh/id_ed25519.pub
# Copy the entire output

# 5. Add to GitHub:
# - Go to: https://github.com/settings/keys
# - Click "New SSH key"
# - Paste the public key
# - Give it a title (e.g., "My MacBook")
# - Click "Add SSH key"

# 6. Test connection
ssh -T git@github.com
# You should see: "Hi USERNAME! You've successfully authenticated..."
```

#### Using SSH with Git:

```bash
# Clone with SSH (not HTTPS)
git clone git@github.com:USERNAME/repo.git

# If you already cloned with HTTPS, change to SSH:
git remote set-url origin git@github.com:USERNAME/repo.git
```

---

### Method 2: GitHub CLI (Alternative)

```bash
# Install (macOS)
brew install gh

# Authenticate securely
gh auth login
# Choose: GitHub.com → SSH → Yes to generate key → Login via browser

# Now you can use gh commands:
gh repo create my-repo --public
gh repo clone USERNAME/repo
```

---

## 🚫 What NEVER to Commit

### 1. API Keys & Tokens

**❌ WRONG:**
```kotlin
// ApiService.kt
const val API_KEY = "abcd1234efgh5678"  // NEVER DO THIS!
```

**✅ CORRECT:**
```kotlin
// ApiService.kt
const val API_KEY = BuildConfig.TMDB_API_KEY  // Loaded from local.properties
```

**How to do it right:**

1. Add to `local.properties` (this file is gitignored):
```properties
TMDB_API_KEY=your_actual_api_key_here
```

2. Read in `build.gradle.kts`:
```kotlin
android {
    defaultConfig {
        // Load from local.properties
        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())
        buildConfigField("String", "TMDB_API_KEY", "\"${properties.getProperty("TMDB_API_KEY")}\"")
    }
}
```

3. Use in code:
```kotlin
val apiKey = BuildConfig.TMDB_API_KEY
```

---

### 2. Signing Keys

**Never commit:**
- `*.jks` (Java KeyStore)
- `*.keystore`
- `key.properties`
- Any file with passwords

---

### 3. Credentials Files

**Never commit:**
- `google-services.json` with real credentials
- `credentials.json`
- `.env` files with secrets
- `secrets.xml`

---

## ✅ Safe Practices Checklist

### Before Every Commit:

```bash
# 1. Check what you're about to commit
git status

# 2. Review the actual changes
git diff

# 3. Look for sensitive data
git diff | grep -i "api_key\|password\|token\|secret"

# 4. If clean, commit
git add .
git commit -m "Your message"
git push
```

---

### After Your First Commit:

```bash
# Check for accidentally committed secrets
git log -p | grep -i "api_key\|password\|token"
```

**If you find something:**

```bash
# Remove from history (BEFORE pushing)
git reset HEAD~1  # Undo last commit
git rm --cached sensitive_file.txt  # Remove from staging
git commit -m "Remove sensitive data"

# If ALREADY pushed (nuclear option):
# 1. Revoke the exposed secret FIRST
# 2. Then use:
git filter-branch --force --index-filter \
  "git rm --cached --ignore-unmatch PATH_TO_FILE" \
  --prune-empty --tag-name-filter cat -- --all
git push origin --force --all
```

---

## 🔍 Tools to Prevent Leaks

### 1. Pre-commit Hooks

Create `.git/hooks/pre-commit`:

```bash
#!/bin/bash

# Check for common secrets
if git diff --cached | grep -E "(api_key|password|secret|token|ghp_)" -i; then
    echo "⚠️  WARNING: Possible secret detected!"
    echo "Review your changes before committing."
    exit 1
fi
```

Make executable:
```bash
chmod +x .git/hooks/pre-commit
```

---

### 2. GitHub Secret Scanning

GitHub automatically scans for:
- GitHub tokens
- AWS keys
- Azure credentials
- Google API keys

If detected, you'll get an alert.

---

## 📧 What to Do If You Exposed a Secret

### Immediate Response:

1. **Revoke the secret** (API key, token, password)
2. **Remove from Git history** (see commands above)
3. **Force push** (if already public)
4. **Generate new secret**
5. **Add to .gitignore** (prevent future exposure)
6. **Update documentation** (how to avoid this)

### Check for Damage:

- Review GitHub security log: https://github.com/settings/security-log
- Check API usage for suspicious activity
- Monitor your email for breach notifications

---

## 📚 Learn More

- [GitHub Security Best Practices](https://docs.github.com/en/code-security)
- [Removing Sensitive Data from Git](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/removing-sensitive-data-from-a-repository)
- [Managing SSH Keys](https://docs.github.com/en/authentication/connecting-to-github-with-ssh)

---

## ✅ Final Checklist Before Going Public

- [ ] All API keys in `local.properties` (gitignored)
- [ ] No hardcoded passwords in code
- [ ] `.gitignore` configured properly
- [ ] SSH keys set up (not using tokens)
- [ ] Pre-commit hook installed
- [ ] Reviewed all files for sensitive data
- [ ] README doesn't expose internal URLs
- [ ] No screenshots with sensitive data

---

## 🎯 Remember:

> **"Once something is on the internet, it's there forever."**
> 
> Even if you delete a commit, someone may have already cloned it.
> **Prevention is better than cleanup.**

---

**Questions? Unsure if something is safe? Ask before committing!**
