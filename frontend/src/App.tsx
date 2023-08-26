import { ReactElement } from 'react';


import { AuthProvider } from './contexts/AuthContext';
import {PageLayout} from "./components/PageLayout";

export default function App(): ReactElement {
  return (
      <AuthProvider>
        <PageLayout />
      </AuthProvider>
  );
}
