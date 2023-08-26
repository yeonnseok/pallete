import { redirectToError, redirectToLogin } from 'utils/auth';
import HttpStatus from 'http-status-codes';

export function postMessageToParent(httpStatus: any): void {
  const numberHttpStatus: number = Number(httpStatus) || HttpStatus.NOT_FOUND;
  window.parent.postMessage(numberHttpStatus, '*');
}

/**
 * 부모 frame 에서 자식 iframe 으로부터 전달받은 messageEvent 를 처리합니다.
 * messageEvent.data 가 httpStatus 인 경우에만 처리하도록 합니다.
 * 아래와 같은 messageEvent 는 무시합니다.
 * messageEvent.data: {
 *   data: undefined,
 *   type: "webpackOk",
 * }
 */
export const messageHandler = function (messageEvent: MessageEvent<any>): void {
  if (typeof messageEvent.data === 'number') {
    const httpStatus: number = messageEvent.data;
    switch (httpStatus) {
      case HttpStatus.UNAUTHORIZED:
        redirectToLogin();
        return;
      default:
        redirectToError(httpStatus);
        return;
    }
  } else {
    return;
  }
};

