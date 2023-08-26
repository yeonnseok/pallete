import { BrowserRouter } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from 'react-query';

import { ErrorResponse } from './common/types';
import App from './App';
import { createRoot } from 'react-dom/client';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      retry: false,
    },
    mutations: {
      useErrorBoundary: false,
      onSuccess: (data) => {
        // user-info인 경우 message를 띄우지 않음
        if (typeof data === 'object' && 'userName' in (data as never) && 'authorities' in (data as never)) {
          return;
        }
      },
      onError: (error) => {
        const { status, data } = (error as ErrorResponse).response;
        if (status === 401) {
          return;
        }
        alert(data.message ?? '요청에 실패했습니다.');
      },
    },
  },
});

const root = createRoot(document.getElementById('root') as any);
root.render(
  <BrowserRouter>
    <QueryClientProvider client={queryClient}>
      <App />
    </QueryClientProvider>
  </BrowserRouter>
);
