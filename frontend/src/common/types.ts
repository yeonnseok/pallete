export type ErrorResponse = Error & {
  response: {
    data: {
      code: string,
      message: null,
    },
    status: number,
  },
};

export interface PageRequest {
  page: number;
  size: number;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  hasNext: boolean;
}

export interface SimpleRole {
  id: string;
  name: string;
  service: string;
  createdAt: string | null;
  createdBy: string | null;
}

export interface SimpleRoleGroup {
  id: string;
  name: string;
  roles: SimpleRole[];
  createdAt: string | null;
  createdBy: string | null;
}
