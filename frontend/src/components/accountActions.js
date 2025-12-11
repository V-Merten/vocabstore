import { useCallback, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { deleteAccount, logout } from '../pages/api.jsx';

export const useAccountActions = () => {
  const navigate = useNavigate();
  const [isDeleting, setIsDeleting] = useState(false);
  const [deleteError, setDeleteError] = useState(null);
  const [logoutError, setLogoutError] = useState(null);

  const handleDeleteAccount = useCallback(async () => {
    setIsDeleting(true);
    setDeleteError(null);
    try {
      await deleteAccount();
      sessionStorage.removeItem('authenticated');
      navigate('/');
    } catch (err) {
      setDeleteError(err.message || 'Failed to delete account');
    } finally {
      setIsDeleting(false);
    }
  }, [navigate]);

  const handleLogout = useCallback(async () => {
    setLogoutError(null);
    try {
      await logout();
    } catch (err) {
      setLogoutError(err.message || 'Failed to logout');
    } finally {
      sessionStorage.removeItem('authenticated');
      navigate('/');
    }
  }, [navigate]);

  const resetDeleteError = useCallback(() => setDeleteError(null), []);

  return {
    isDeleting,
    deleteError,
    logoutError,
    handleDeleteAccount,
    handleLogout,
    resetDeleteError,
  };
};
