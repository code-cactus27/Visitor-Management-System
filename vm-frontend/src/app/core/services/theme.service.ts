import { Injectable } from '@angular/core';
@Injectable({
  providedIn: 'root'
})
export class ThemeService {
  isDark = true;
  toggleTheme() {
    this.isDark = !this.isDark;
    if (this.isDark) {
      document.body.classList.add('bg-dark', 'text-white');
      document.body.classList.remove('bg-light', 'text-dark');
      localStorage.setItem('theme', 'dark');
    } else {
      document.body.classList.add('bg-light', 'text-dark');
      document.body.classList.remove('bg-dark', 'text-white');
      localStorage.setItem('theme', 'light');
    }
  }
  loadTheme() {
    const saved = localStorage.getItem('theme');
    if (saved === 'light') {
      this.isDark = false;
      document.body.classList.add('bg-light', 'text-dark');
    } else {
      document.body.classList.add('bg-dark', 'text-white');
    }
  }
}
