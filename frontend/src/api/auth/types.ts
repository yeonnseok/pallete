export interface OneTimePasswordRequest {
  menu: string;
  service: string;
}

export interface OneTimePasswordResponse {
  oneTimePassword: string;
}
