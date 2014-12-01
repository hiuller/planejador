## Copyright (C) 2014 Usiminas
## 
## This program is free software; you can redistribute it and/or modify
## it under the terms of the GNU General Public License as published by
## the Free Software Foundation; either version 3 of the License, or
## (at your option) any later version.
## 
## This program is distributed in the hope that it will be useful,
## but WITHOUT ANY WARRANTY; without even the implied warranty of
## MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
## GNU General Public License for more details.
## 
## You should have received a copy of the GNU General Public License
## along with Octave; see the file COPYING.  If not, see
## <http://www.gnu.org/licenses/>.

## simao

## Author: Usiminas <Usiminas@USIMINAS-HP>
## Created: 2014-10-21

  clear all;

aft = [
3600 8100 9300 10400 10400 8550 9400 10200 10400 10400 10400 10400 4600 7000 9900 10400 10400 9900 9900 8900 8600 9600 10100 10400 10400 10400 4600 3600 8100 9300 10400
]'; % producao total dos alto fornos 

hic = [ 0 0 0 0 0 0 0 0 0 0 0 0 0 4 0 0 0 0 0 0 0 4 0 0  4 0 4 0 0 0 0  ]';
dr  = [12 5 2 0 5 16 4 13 5 2 16 4 8 1 9 10 16 14 6 5 7 15 5 1 8 14 11 2 4 13 13 ]';              % corridas duplo refino
rh  = [12 8 18 29 9 22 13 17 13 29 28 24 1 29 7 18 17 26 21 9 4 15 19 6 21 25 21 19 3 26 20]';    % corridas rh
fp  = [9 1 0 2 2 8 1 2 6 4 8 7 7 0 2 0 5 0 2 3 5 2 2 0 5 0 1 0 1 2 4]';                           % corridas fp ac2
fp1 = [0 0 0 0 0 0 0 0 0 0 0 0 7 7 7 7 7 7 7 7 7 7 7 7 7 7 7 7 7 7 7]';                           % corridas fp ac1

% vetor com as cargas medias
  pc_ac1_fp     =  74.0
  pc_ac1_comum  =  76.0
  pc_ac1_trans  =  71.0
  pc_ac2_hic    = 163.0
  pc_ac2_dr     = 164.0
  pc_ac2_rh     = 165.0
  pc_ac2_fp     = 166.0
  pc_ac2_comum  = 167.0

  pc1 = [pc_ac1_fp pc_ac1_trans pc_ac1_comum];
  pc2 = [pc_ac2_hic pc_ac2_dr pc_ac2_rh pc_ac2_fp pc_ac2_comum];
  
  clear pc_ac1_fp pc_ac1_comum pc_ac1_trans pc_ac2_hic pc_ac2_dr pc_ac2_rh pc_ac2_fp pc_ac2_comum;

s0 = 1500.0 % saldo inicial
rend1 = 

  a=[1 1 1 0.333 1];
  p=[4 14 16];

      function corridas = produtividade(a, p)
        % a - vetor de disponibilidade (também pode ser chamado de a)
        % p - vetor de restritivos (DR, RH, FP)

        % parametros do modelo
          Theta = [11.181 28.376 -0.142 0.369 -11.320 1.197];
          r     = [-0.215 -0.615 -0.079];
        
            a_aug = [ones(size(a, 1),1) a];
            h = a_aug * Theta';      
            c = p * r';      
            corridas = h .- c;
            
      endfunction
      
  printf('\n produtividade para (a,p): %.0f corridas\n\n', round(produtividade(a, p)) )
  dias_hic = (1:31)'(hic==4)

  
  