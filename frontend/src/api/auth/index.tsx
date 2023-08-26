import { useMutation, UseMutationResult } from 'react-query';
import { authClient } from '../client';
import { AxiosResponse } from 'axios';
import { OneTimePasswordRequest, OneTimePasswordResponse } from 'api/auth/types';

export async function fetchPortalToken(): Promise<AxiosResponse<String>> {
  return await authClient.get('/v1/auth/portal-token');
}
