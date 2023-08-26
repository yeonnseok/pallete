// @ts-ignore
import React, {ReactElement} from 'react';

import {useAuth} from '../contexts/AuthContext';
import {Layout, Menu, MenuProps, theme} from "antd";
import Sider from "antd/lib/layout/Sider";
import {Content, Footer, Header} from "antd/lib/layout/layout";
import {LogoutOutlined} from "@ant-design/icons";
import {Route, Routes, useNavigate} from 'react-router-dom';
import {Main} from "../pages/main";
import {NotFoundErrorPage} from "../pages/NotFoundErrorPage";
import {LoginPage} from "../pages/auth/LoginPage";
import {SignupPage} from "../pages/auth/SignupPage";

export function PageLayout(): ReactElement {
  const {nickname, logout, isLogin} = useAuth();

  const {Header, Content, Sider} = Layout;

  const rightMenusForLogin: MenuProps['items'] = [
    {
      key: 'profile',
      label: nickname,
    },
    {
      key: 'logout',
      label: <LogoutOutlined/>,
    }
  ]

  const rightMenusForNotLogin: MenuProps['items'] = [
    {
      key: 'login',
      label: `로그인`,
    },
    {
      key: 'signup',
      label: `회원가입`,
    }
  ]

  const {
    token: {colorBgContainer},
  } = theme.useToken();

  const navigate = useNavigate();

  const handleGnbMenuClick = (e: any) => {
    switch (e.key) {
      case 'login':
        navigate('/login');
        break;
      case 'signup':
        navigate('/signup');
        break;
      case 'logout':
        break;
      case 'profile':
        navigate(`/members/${nickname}`)
        break;
      default:
        navigate('/');
    }
  };

  return (
    <Layout>
      <Header style={{display: 'flex', alignItems: 'center'}}>
        <div className="demo-logo"/>
        <Menu theme="dark" mode="horizontal" defaultSelectedKeys={['2']} items={[]}/>
        <Menu
          theme="dark"
          mode="horizontal"
          style={{marginLeft: 'auto'}}
          items={isLogin ? rightMenusForLogin : rightMenusForNotLogin}
          onClick={handleGnbMenuClick}
        />
      </Header>
      <Layout>
        { isLogin && (
          <Sider width={200} style={{background: colorBgContainer}}>
            <Menu
              mode="inline"
              defaultSelectedKeys={['1']}
              defaultOpenKeys={['sub1']}
              style={{height: '100%', borderRight: 0}}
              items={[]}
            />
          </Sider>
        )}
        <Layout style={{padding: '0 24px 24px', minHeight: '100vh'}}>
          <Content
            style={{
              padding: 24,
              margin: 0,
              minHeight: 280,
              background: colorBgContainer,
            }}
          >
            <Routes>
              <Route path="/" element={<Main/>}/>
              <Route path="/login" element={<LoginPage/>}/>
              <Route path="/signup" element={<SignupPage/>}/>
              <Route path="/*" element={<NotFoundErrorPage/>}/>
            </Routes>
          </Content>
        </Layout>
      </Layout>
    </Layout>
  );
}
