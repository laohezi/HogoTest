#!/usr/bin/env node
const { execSync } = require('child_process');

const PKG = 'com.example.hogotest';
const ACT = '.MainActivity';

function sh(cmd, opts = {}) {
  console.log(`$ ${cmd}`);
  execSync(cmd, { stdio: 'inherit', ...opts });
}

try {
  // Launch the app's main activity
  sh(`adb shell am start -n ${PKG}/${ACT}`);
} catch (e) {
  process.exit(typeof e.status === 'number' ? e.status : 1);
}
