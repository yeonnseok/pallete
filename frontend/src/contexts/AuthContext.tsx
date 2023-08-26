import { createContext, ReactElement, ReactNode, useContext, useMemo } from 'react';


type AuthContext = {
  nickname: string | null,
  logout: () => void,
  isLogin: boolean,
};

export const AuthContext = createContext<AuthContext>({
  nickname: null,
  logout: () => {
  },
  isLogin: false,
});

type AuthProviderProps = {
  children: ReactNode,
};

export function AuthProvider({ children }: AuthProviderProps): ReactElement {

  const logout = (): void => {
    localStorage.removeItem("backoffice");
  };

  const value = useMemo(() => {
    return { nickname: 'test' ?? null, logout, isLogin: false };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <AuthContext.Provider value={value}>
      { children }
    </AuthContext.Provider>
  );
}

export function useAuth(): AuthContext {
  return useContext(AuthContext);
}
