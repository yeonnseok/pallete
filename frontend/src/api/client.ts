import axios from 'axios';

import {
  API_URL,
  AUTH_HEADER,
  BACKOFFICE_AUTH_HEADER,
  BACKOFFICE_TOKEN_KEY,
  BEARER_AUTH_SCHEME,
  PORTAL_TOKEN_KEY,
} from 'common/constants';

import { redirectToLogin } from 'utils/auth';

const TIMEOUT = 10000;
const headers = {
  'Accept': 'application/json',
  'Content-Type': 'application/json',
};

/**
 * Admin Portal API 를 호출하는 AxiosInstance 입니다.
 * 인증에 portalToken 을 사용합니다.
 */
export const client = axios.create({
  baseURL: `${API_URL}/api`,
  timeout: TIMEOUT,
  headers,
  withCredentials: false,
});

/**
 * Client Request Interceptor 설정입니다.
 * 1. portalToken 이 있을 경우 header 에 추가합니다.
 * 2. portalToken 이 없을 경우 getPortalToken 을 호출합니다.
 * 2-1. getPortalToken 의 결과가 성공일 경우 header 에 추가합니다.
 * 2-2. getPortalToken 의 결과가 실패일 경우 backoffice 로그인 페이지로 이동합니다.
 */
client.interceptors.request.use(
  async (config) => {
    const portalToken = localStorage.getItem(PORTAL_TOKEN_KEY);
    if (portalToken) {
      config.headers[AUTH_HEADER] = `${BEARER_AUTH_SCHEME} ${portalToken}`;
      return config;
    }
  },
  (error) => {
    return Promise.reject(error);
  }
);

/**
 * Client Response Interceptor 설정입니다.
 * 1. 유효하지 않은 PortalToken 인 경우
 * 1-1. Client Request Interceptor 에서 처리됩니다. backoffice 로그인 페이지로 이동합니다.
 * 2. 유효하나, 인가되지 않는 PortalToken 인 경우
 * 2-1. "해당 요청에 대한 권한이 없습니다." 라는 메시지를 띄웁니다.
 */
client.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    if (error.response && error.response.status === 401) {
      localStorage.removeItem(PORTAL_TOKEN_KEY);
      redirectToLogin();
      return error;
    }

    if (error.response && error.response.status === 403) {
      alert('요청에 대한 권한이 없습니다.');
    }

    if (error.response && error.response.status === 400) {
      alert('요청이 올바르지 않습니다.');
    }

    return error.response;
  }
);

/**
 * getPortalToken 을 호출하는 AxiosInstance 입니다.
 * 인증에 backofficeToken 을 사용합니다.
 */
export const authClient = axios.create({
  baseURL: `${API_URL}/api`,
  timeout: TIMEOUT,
  headers,
  withCredentials: false,
});

/**
 * AuthClient Request Interceptor 설정입니다.
 */
authClient.interceptors.request.use(
  (config) => {
    const backofficeToken = localStorage.getItem(BACKOFFICE_TOKEN_KEY);
    if (backofficeToken) {
      config.headers[BACKOFFICE_AUTH_HEADER] = backofficeToken;
      return config;
    } else {
      throw new Error('Backoffice header is missing.');
    }
  },
  (error) => {
    error.name = `[AuthClient Request] ${error.name}`;
    return Promise.reject(error);
  }
);

/**
 * AuthClient Response Interceptor 설정입니다.
 * 1. backofficeToken 이 유효하지 않은 경우
 * 1-1. token이 틀린 형식, 만료, 위조된 경우
 * 1-2. token은 유효하나, 사용자가 없는 경우
 */
authClient.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    error.name = `[AuthClient Response] ${error.name}`;
    return Promise.reject(error);
  }
);
