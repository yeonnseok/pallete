import { IStringifyOptions } from 'qs';

export const BACKOFFICE_TOKEN_KEY = 'backoffice-auth-token';
export const PORTAL_TOKEN_KEY = 'portal-auth-token';
export const AUTH_HEADER = 'Authorization';
export const BEARER_AUTH_SCHEME = 'Bearer';
export const BACKOFFICE_AUTH_HEADER = 'X-Backoffice-Auth-Token';
export const API_URL = process.env.REACT_APP_API_URL;
export const AUTH_URL = process.env.REACT_APP_AUTH_URL;
export const WEB_URL = process.env.REACT_APP_WEB_URL;

export const SEARCH_PARAMS_FORMAT: IStringifyOptions = {
  arrayFormat: 'comma',
  encode: false, skipNulls: true,
};
