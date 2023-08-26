import { AUTH_URL, WEB_URL } from 'common/constants';

export function redirectToLogin(): void {
  const slashTrimmedUrl = window.location.href.replace(/\/*$/, '');
  window.location.replace(`${AUTH_URL}/login?url=${slashTrimmedUrl}`);
}

export function redirectToError(status: number): void {
  window.location.replace(`${WEB_URL}/error/${status}`);
}
