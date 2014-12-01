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

## teste

## Author: Usiminas <Usiminas@USIMINAS-HP>
## Created: 2014-09-17

    clear all;

            a_dia_normal      = [ 1.00, 1.00, 1.00, 1.00, 1.00 ];
            a_dia_campanha    = [ 0.50, 1.00, 1.00, 1.00, 1.00 ];
            a_dia_parada_aci  = [ 0.02, 0.02, 0.02, 0.02, 0.02 ];
            a_parada_rh       = [ 1.00, 1.00, 1.00, 0.33, 1.00 ];  %% dia de parada do RH
            a_dia_insp_maq    = [ 1.00, 1.00, 1.00, 1.00, 0.45 ];
            a_dia_parada_maq  = [ 1.00, 1.00, 1.00, 1.00, 0.65 ];

          A = [
                a_dia_normal      ;
                a_dia_campanha    ;
                a_dia_parada_aci  ;
                a_parada_rh       ;  
                a_dia_insp_maq    ;
                a_dia,_parada_maq  ];
            
        %%    DR, RH, FP
              dia_normal      = [ 14 16 06 ];
              dia_apertado    = [ 08 22 02 ];
              dia_campanha    = [ 01 28 01 ];
              dia_parada_aci  = [ 01 10 01 ];
              dia_parada_rh   = [ 04 14 16 ];
              dia_insp_maq    = [ 01 30 04 ];
              dia_parada_maq  = [ 01 30 01 ];    
              
          P = [
                dia_normal      ;
                dia_campanha    ;
                dia_parada_aci  ;
                dia_parada_rh   ;
                dia_insp_maq    ;
                dia_parada_maq  ];
          
            prod_normal     = 43;
            prod_apertado   = 48;
            prod_campanha   = 33;
            prod_parada_aci = 18;
            prod_parada_rh  = 48;
            prod_insp_maq   = 48;
            prod_parada_maq = 48;

    Y=[ prod_normal; prod_campanha; prod_parada_aci; prod_parada_rh; prod_insp_maq; prod_parada_maq ];

    
%%%%%%%%%%%%%%%  Parameters  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    param = rand(1, 9);
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%    
    
    
  function J = computeCost(Y, Y_bar)
      
      d = Y .- Y_bar;
      J = d'*d;
      
  endfunction

  function Y_bar = predict(param, A, P)  
      Theta = param(1:6);
      r     = param(7:9);
      
      a_aug = [ones(size(A, 1),1) A];
      
      H = a_aug * Theta';
      
      C = P * r';
      
      Y_bar = H .- C;     
  endfunction
  
  
  
  
  fcn = @(p) computeCost(Y, predict(p, A, P));
  
  result = fminunc(fcn, param)  
  
  printf('validacao ');
  round([ Y predict(result, A, P) ])
  
  printf('prevendo o dia apertado ');
  round([ prod_apertado predict(result, a_dia_normal, dia_apertado) ])
  
  
% save to disc
## save params result